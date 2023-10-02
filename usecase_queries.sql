--Function created to swap a shift without labtechs

CREATE OR REPLACE FUNCTION RequestShiftSwap(
    n_request_id UUID,
    requestor_id UUID,
    reason VARCHAR(255),
    request_status VARCHAR(255),
    time_created TIMESTAMP,
    time_resolved TIMESTAMP,
    requested_shift_id UUID,
    proposed_shift_id UUID
)
    RETURNS TEXT AS $$
DECLARE
    message TEXT;
BEGIN
    -- Check if the request already exists
    IF EXISTS (
        SELECT 1
        FROM abstract_requests ar
        WHERE ar.request_id = n_request_id
    ) THEN
        message := 'A request with this ID already exists.';
        -- Check if the Lab Tech has the requested shift
    ELSIF EXISTS (
        SELECT 1
        FROM shifts_labtechs sl
        WHERE sl.user_id = requestor_id
          AND sl.shift_id = proposed_shift_id
    ) THEN
        IF EXISTS (
            SELECT 1
            FROM shifts_labtechs sl
            WHERE sl.shift_id = requested_shift_id
        ) THEN
            -- Insert the shift swap request

            INSERT INTO abstract_requests (request_id, requestor_id, reason, request_status, time_created, time_resolved)
            VALUES (n_request_id, requestor_id, reason, request_status, time_created, time_resolved);

            INSERT INTO shift_swap_requests (request_id, requested_shift_id, proposed_shift_id)
            VALUES (n_request_id, requested_shift_id, proposed_shift_id);

            INSERT INTO request_approvers (user_id, request_id)
            SELECT DISTINCT sl.user_id, n_request_id
            FROM shifts_labtechs sl
            WHERE sl.shift_id = requested_shift_id;

            message := 'Shift swap request created successfully.';
        ELSE
            message := 'You do not have the proposed shift or the requested shift does not exist.';
        END IF;
    ELSE
        message := 'The request does not exist and you do not have the proposed shift.';
    END IF;

    RETURN message;
END;
$$ LANGUAGE plpgsql;


SELECT RequestShiftSwap(
               '123e4567-e89b-12d3-a456-426655440000',
               'b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5',
               'My wife will be entering labour, and I need to be there.',
               'PENDING',
               '2023-09-22 10:00:00',
               null,
               'a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8',
               '123e4567-e89b-12d3-a456-426655440000'
           );



--Function created to view all labtechs working on a particular shift

CREATE OR REPLACE FUNCTION ViewShiftLabtechs (
    n_shift_id UUID
)
    RETURNS TABLE (
                      user_id UUID,
                      university_id VARCHAR(20),
                      first_name VARCHAR(100),
                      last_name VARCHAR(100),
                      user_role VARCHAR(20),
                      phone_num VARCHAR(20),
                      email VARCHAR(20)
                  ) AS $$
BEGIN
    -- Check if the shift exists
    IF EXISTS (
        SELECT 1
        FROM shifts_labtechs sl
        WHERE sl.shift_id = n_shift_id
    ) THEN
        RETURN QUERY
            SELECT u.user_id, u.university_id, u.first_name, u.last_name, u.user_role, u.phone_num, u.email
            FROM shifts_labtechs sl
                     JOIN users u ON u.user_id = sl.user_id
            WHERE sl.shift_id = n_shift_id;
    ELSE
        RETURN QUERY
            SELECT NULL::UUID, NULL::TEXT, NULL::TEXT, NULL::TEXT, NULL::TEXT, NULL::TEXT, NULL::TEXT;
    END IF;
END;
$$ LANGUAGE plpgsql;


SELECT *
FROM ViewShiftLabtechs('123e4567-e89b-12d3-a456-426655440000');


--Function to view User's requests (made by them)
CREATE OR REPLACE FUNCTION ViewMyShiftRequests (
    n_user_id UUID,
    n_request_status VARCHAR(255)
)
    RETURNS TABLE (
                      request_id UUID,
                      requestor_id UUID,
                      reason VARCHAR(255),
                      request_status VARCHAR(255),
                      time_created TIMESTAMP,
                      time_resolved TIMESTAMP,
                      shift_id UUID,
                      start_time TIMESTAMP,
                      end_time TIMESTAMP,
                      location_id UUID,
                      room_name VARCHAR(255),
                      virtual_link VARCHAR(100)
                  ) AS $$
BEGIN
    -- Check if the shift exists
    IF EXISTS (
        SELECT 1
        FROM abstract_requests ar
                 JOIN shift_swap_requests swr ON ar.request_id = swr.request_id
        WHERE ar.requestor_id = n_user_id
    ) THEN
        RETURN QUERY
            SELECT ar.request_id, ar.requestor_id, ar.reason, ar.request_status,
                   ar.time_created, ar.time_resolved, s.shift_id, s.start_time,
                   s.end_time, l.location_id, l.room_name, l.virtual_link
            FROM abstract_requests ar
                     JOIN shift_swap_requests swr ON ar.request_id = swr.request_id
                     JOIN shifts s ON swr.requested_shift_id = s.shift_id
                OR swr.proposed_shift_id = s.shift_id
                     JOIN locations l ON s.location_id = l.location_id
            WHERE ar.request_status = n_request_status;
    ELSE
        RETURN QUERY
            SELECT NULL::UUID,
                   NULL::UUID,
                   NULL::VARCHAR(255),
                   NULL::VARCHAR(255),
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::UUID,
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::UUID,
                   NULL::VARCHAR(255),
                   NULL::VARCHAR(100);
    END IF;
END;
$$ LANGUAGE plpgsql;


SELECT *
FROM ViewMyShiftRequests('b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5', 'PENDING');

--Function to view User's requests (Ones they need to approve or decline)
CREATE OR REPLACE FUNCTION ViewOtherShiftRequests (
    n_user_id UUID,
    n_request_status VARCHAR(255)
)
    RETURNS TABLE (
                      request_id UUID,
                      requestor_id UUID,
                      reason VARCHAR(255),
                      request_status VARCHAR(255),
                      time_created TIMESTAMP,
                      time_resolved TIMESTAMP,
                      shift_id UUID,
                      start_time TIMESTAMP,
                      end_time TIMESTAMP,
                      location_id UUID,
                      room_name VARCHAR(255),
                      virtual_link VARCHAR(100)
                  ) AS $$
BEGIN
    -- Check if the shift exists
    IF EXISTS (
        SELECT 1
        FROM request_approvers ra
        WHERE ra.user_id = n_user_id
    ) THEN
        RETURN QUERY
            SELECT ar.request_id, ar.requestor_id, ar.reason, ar.request_status,
                   ar.time_created, ar.time_resolved, s.shift_id, s.start_time,
                   s.end_time, l.location_id, l.room_name, l.virtual_link
            FROM request_approvers ra
                     JOIN abstract_requests ar ON ra.request_id = ar.request_id
                     JOIN shift_swap_requests swr ON ar.request_id = swr.request_id
                     JOIN shifts s ON swr.requested_shift_id = s.shift_id
                OR swr.proposed_shift_id = s.shift_id
                     JOIN locations l ON s.location_id = l.location_id
            WHERE ar.request_status = n_request_status;
    ELSE
        RETURN QUERY
            SELECT NULL::UUID,
                   NULL::UUID,
                   NULL::VARCHAR(255),
                   NULL::VARCHAR(255),
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::UUID,
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::UUID,
                   NULL::VARCHAR(255),
                   NULL::VARCHAR(100);
    END IF;
END;
$$ LANGUAGE plpgsql;


SELECT *
FROM ViewOtherShiftRequests('b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5', 'PENDING');

--Function created to request time off

CREATE OR REPLACE FUNCTION RequestTimeOff(
    n_request_id UUID,
    requestor_id UUID,
    reason VARCHAR(255),
    request_status VARCHAR(255),
    time_created TIMESTAMP,
    time_resolved TIMESTAMP,
    start_date TIMESTAMP,
    end_date TIMESTAMP
)
    RETURNS TEXT AS $$
DECLARE
    message TEXT;
BEGIN
    -- Check if the request already exists
    IF EXISTS (
        SELECT 1
        FROM abstract_requests ar
        WHERE ar.request_id = n_request_id
    ) THEN
        message := 'A request with this ID already exists.';
    ELSE
        -- Insert the time off request

        INSERT INTO abstract_requests (request_id, requestor_id, reason, request_status, time_created, time_resolved)
        VALUES (n_request_id, requestor_id, reason, request_status, time_created, time_resolved);

        INSERT INTO time_off_requests (request_id, start_date, end_date)
        VALUES (n_request_id, start_date, end_date);

        message := 'Time off request created successfully.';
    END IF;

    RETURN message;
END;
$$ LANGUAGE plpgsql;


SELECT RequestTimeOff(
               '123e4567-e89b-12d3-a456-426655440111',
               'b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5',
               'My wife will be entering labour, and I need to be there.',
               'PENDING',
               '2023-09-22 10:00:00',
               null,
               '2023-09-22 10:00:00',
               '2023-09-25 10:00:00'
           );


--Function to view Time OFF requests (Ones the labmanagers need to approve or decline)
CREATE OR REPLACE FUNCTION ViewTimeOffRequests (

    n_request_status VARCHAR(255)
)
    RETURNS TABLE (
                      request_id UUID,
                      requestor_id UUID,
                      reason VARCHAR(255),
                      request_status VARCHAR(255),
                      time_created TIMESTAMP,
                      time_resolved TIMESTAMP,
                      start_date TIMESTAMP,
                      end_date TIMESTAMP

                  ) AS $$
BEGIN
    -- Check if the shift exists
    IF EXISTS (
        SELECT 1
        FROM abstract_requests ab
                 JOIN time_off_requests tor ON ab.request_id = tor.request_id
        WHERE ab.request_status = n_request_status
    ) THEN
        RETURN QUERY
            SELECT ar.request_id, ar.requestor_id, ar.reason, ar.request_status,
                   ar.time_created, ar.time_resolved, tor.start_date, tor.end_date
            FROM abstract_requests ar
                     JOIN time_off_requests tor ON ar.request_id = tor.request_id
            WHERE ar.request_status = n_request_status;
    ELSE
        RETURN QUERY
            SELECT NULL::UUID,
                   NULL::UUID,
                   NULL::VARCHAR(255),
                   NULL::VARCHAR(255),
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP,
                   NULL::TIMESTAMP;
    END IF;
END;
$$ LANGUAGE plpgsql;


SELECT *
FROM ViewTimeOffRequests('PENDING');

--Function to approve a request
CREATE OR REPLACE FUNCTION ApproveRequest(
    n_request_id UUID
)
    RETURNS TEXT AS $$
DECLARE
    message TEXT;
    request_exists BOOLEAN;
BEGIN
    -- Check if request exists
    SELECT EXISTS (
        SELECT 1
        FROM abstract_requests
        WHERE request_id = n_request_id
    ) INTO request_exists;

    IF request_exists THEN
        -- Change request status to approved
        UPDATE abstract_requests
        SET request_status = 'APPROVED'
        WHERE request_id = n_request_id;

        message := 'Status Changed Successfully';
    ELSE
        message := 'Request does not exist';
    END IF;

    RETURN message;
END;
$$ LANGUAGE plpgsql;

SELECT ApproveRequest('123e4567-e89b-12d3-a456-426655440000');


--Function to deny a request

CREATE OR REPLACE FUNCTION DenyRequest(
    n_request_id UUID
)
    RETURNS TEXT AS $$
DECLARE
    message TEXT;
    request_exists BOOLEAN;
BEGIN
    -- Check if request exists
    SELECT EXISTS (
        SELECT 1
        FROM abstract_requests
        WHERE request_id = n_request_id
    ) INTO request_exists;

    IF request_exists THEN
        -- Change request status to DENIED
        UPDATE abstract_requests
        SET request_status = 'DENIED'
        WHERE request_id = n_request_id;

        message := 'Status Changed Successfully';
    ELSE
        message := 'Request does not exist';
    END IF;

    RETURN message;
END;
$$ LANGUAGE plpgsql;

SELECT DenyRequest('123e4567-e89b-12d3-a456-426655440000');

--Function to set a request to PENDING

CREATE OR REPLACE FUNCTION PendRequest(
    n_request_id UUID
)
    RETURNS TEXT AS $$
DECLARE
    message TEXT;
    request_exists BOOLEAN;
BEGIN
    -- Check if request exists
    SELECT EXISTS (
        SELECT 1
        FROM abstract_requests
        WHERE request_id = n_request_id
    ) INTO request_exists;

    IF request_exists THEN
        -- Change request status to DENIED
        UPDATE abstract_requests
        SET request_status = 'PENDING'
        WHERE request_id = n_request_id;

        message := 'Status Changed Successfully';
    ELSE
        message := 'Request does not exist';
    END IF;

    RETURN message;
END;
$$ LANGUAGE plpgsql;

SELECT PendRequest('123e4567-e89b-12d3-a456-426655440000');



