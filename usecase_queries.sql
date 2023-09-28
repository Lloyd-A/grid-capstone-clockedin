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

select * from abstract_requests;

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
