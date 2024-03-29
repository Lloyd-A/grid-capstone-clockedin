package com.clockedIn.shiftservice.repositories;

import com.clockedIn.shiftservice.model.Course;
import com.clockedIn.shiftservice.model.Shift;
import java.util.UUID;

public interface ShiftRepository extends Repository<Shift, UUID> {

    Iterable<Shift> findShiftsByCourse(Course course);

}
