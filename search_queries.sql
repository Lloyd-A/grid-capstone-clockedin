-- Search query with dynamic filters, pagination and sorting

-- 1) Function that enables dynamic queries

CREATE OR REPLACE FUNCTION dynamic_search(
    column_name TEXT,
   	filter_value text,
   	order_by_criteria text,
   	page_size INT
)
returns table (result_column UUID, start_time TIMESTAMP, end_time TIMESTAMP, location_id UUID, course_enum VARCHAR) as $$
BEGIN
	return QUERY execute
		format('SELECT * FROM shifts WHERE %I = %L ORDER BY %I LIMIT %L',
	column_name, filter_value, order_by_criteria, page_size);
end;
$$ language plpgsql

-- SQL query to insert arguments into dynamic query: searching for shift_id, sorting by course and pagination of size 10

select * from dynamic_search('shift_id', '123e4567-e89b-12d3-a456-426655440000', 'course_enum', 10);

-- 2) Search query with joined data for your use-cases

-- finds labtechs' names for a shift with id 'a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8'
select first_name, last_name from users u join
shifts_labtechs sl on u.user_id = sl.user_id join
shifts s on sl.shift_id = s.shift_id
where sl.shift_id = 'a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8';

-- 3) Statistic query; can be not related to your use-cases

-- returns labtechs and number of shifts they have
select first_name, last_name, count(sl.shift_id) from users u join
shifts_labtechs sl on u.user_id = sl.user_id group by
first_name, last_name;

-- 4) Top-something query

-- returns labtechs and number of shifts they have in descending order
select first_name, last_name, count(sl.shift_id) as counter from users u join
shifts_labtechs sl on u.user_id = sl.user_id group by
first_name, last_name order by
counter DESC;