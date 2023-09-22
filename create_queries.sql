--Create Query for a location

INSERT INTO "locations" ("location_id", "room_name", "virtual_link")
VALUES ('1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f',
		'SLT2', null);

--Create Query for a shift

INSERT INTO "shifts" ("shift_id", "start_time", "end_time", "location_id", "course_enum")
VALUES ('a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8', '2023-09-25 08:00:00', '2023-09-25 09:00:00', '<location_id_value>', 'COMP1161');

INSERT INTO shifts (shift_id, start_time, end_time, location_id, course_enum)
VALUES ('123e4567-e89b-12d3-a456-426655440000', '2023-09-28 08:00:00', '2023-09-28 09:00:00', '1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f', 'COMP1161');



--Create Query for two users

INSERT INTO "users" ("user_id", "university_id", "first_name", "last_name", "user_role", "phone_num", "email")
VALUES ('b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5', '620111055', 'Lloyd', 'Allen', 'LABTECH', '876-666-5555', 'lallen@gmail.com');

INSERT INTO "users" ("user_id", "university_id", "first_name", "last_name", "user_role", "phone_num", "email")
VALUES ('b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5', '620111099', 'Raheed', 'Gilzene', 'LABTECH', '876-777-6666', 'rgilzene@gmail.com');

--Create Query for two labtechs

INSERT INTO "labtechs" ("user_id")
VALUES ('b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5');

INSERT INTO "labtechs" ("user_id")
VALUES ('b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5');


-- Create query for shifts_labtech

INSERT INTO "shifts_labtechs" ("shift_id", "user_id")
VALUES ('a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8', 'b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5');

INSERT INTO "shifts_labtechs" ("shift_id", "user_id")
VALUES ('123e4567-e89b-12d3-a456-426655440000', 'b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5');

-- Create query for abstract_requests

INSERT INTO abstract_requests (request_id, requestor_id, reason, request_status, time_created, time_resolved)
VALUES ('123e4567-e89b-12d3-a456-426655440000', 'b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5', 'My wife will be entering labour, and I need to be there.', 'PENDING', '2023-09-22 10:00:00', null);



