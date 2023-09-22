-- Search query with dynamic filters, pagination and sorting
-- Shift By Course

SELECT * FROM shifts s JOIN locations l ON s.location_id = l.location_id
    WHERE (s.course_enum = :course_enum OR :course_enum IS NULL)
    AND (s.start_time = :start_time OR :start_time IS NULL)
    AND (s.end_time = :end_time OR :end_time IS NULL)
    AND (l.room_name = :room_name OR :room_name IS NULL)
    AND (l.virtual_link = :virtual_link OR :virtual_link IS NULL);


 CREATE OR REPLACE FUNCTION dynamic_search(

 )
