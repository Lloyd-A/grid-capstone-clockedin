--Update Query for a location
UPDATE locations
SET "virtual_link" = 'http://www.example.com'
WHERE "location_id" = '1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f';

--Update Query for a shift
UPDATE "shifts"
SET "course_enum" = 'COMP1127'
WHERE "shift_id" = '123e4567-e89b-12d3-a456-426655440000';

--Update Query for abstract_requests
UPDATE abstract_requests
SET "reason" = "Truth is I'm, options are few."
WHERE "request_id" = '123e4567-e89b-12d3-a456-426655440000';


