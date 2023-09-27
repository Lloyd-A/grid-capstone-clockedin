CREATE TABLE shifts (
    shift_id INT PRIMARY KEY,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    fk_location_room_name VARCHAR(50),
    fk_course_code VARCHAR(50),
    FOREIGN KEY (fk_location_room_name) REFERENCES locations(room_name),
    FOREIGN KEY (fk_course_code) REFERENCES courses(code)
);

CREATE TABLE locations (
    room_name VARCHAR(50) PRIMARY KEY,
    virtual_link VARCHAR(100)
);

CREATE TABLE courses (
    code VARCHAR(8)
);