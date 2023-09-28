--Delete Query for a location
DELETE FROM "locations"
WHERE "location_id" = '1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f';

--Delete Query for a shift
DELETE FROM "shifts"
WHERE "shift_id" = 'a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8';

--Delete Query for a user
DELETE FROM "users"
WHERE "user_id" = 'b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5';

--Delete Query for a labtech
DELETE FROM "labtechs"
WHERE "user_id" = 'b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5';

--Delete Query for a shifts_labtechs entry
DELETE FROM "shifts_labtechs"
WHERE "shift_id" = 'a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8' AND "user_id" = 'b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5';

--Delete Query for a abstract_requests
DELETE FROM abstract_requests
WHERE "request_id" = '123e4567-e89b-12d3-a456-426655440000';

--Delete Query for a shift_swap_requests
DELETE FROM shift_swap_requests
WHERE "request_id" = '123e4567-e89b-12d3-a456-426655440000';



