package com.clockedIn.shiftservice.DAO;

import com.clockedIn.shiftservice.model.Course;
import com.clockedIn.shiftservice.model.Location;
import com.clockedIn.shiftservice.model.Shift;
import com.clockedIn.shiftservice.util.objectmappers.ShiftMapper;
import util.ConnectionCreationAndUsage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class ShiftDAO {

    public Optional<Shift> getShiftById(UUID shiftId) {
        String getShiftQuery = "SELECT * FROM shifts s JOIN locations l ON s.location_id=l.location_id " +
                "WHERE shift_id = ?";
        Function<ResultSet, Shift> mapper = ShiftMapper.map();

        Shift shift = ConnectionCreationAndUsage.findOne(getShiftQuery, mapper, shiftId);

        return Optional.ofNullable(shift);
    }

    public Optional<Shift> getShiftByCourse(Course course) {
        String getShiftByCourseQuery = "SELECT * FROM shifts WHERE course_enum = ?";
        Function<ResultSet, Shift> mapper = ShiftMapper.map();

        Shift shift = ConnectionCreationAndUsage.findOne(getShiftByCourseQuery, mapper, course);

        return Optional.ofNullable(shift);
    }

    public Iterable<Shift> getAllShifts() {
        String getAllShiftsQuery = "SELECT * FROM shifts s JOIN locations l ON s.location_id=l.location_id";
        Function<ResultSet, Shift> mapper = ShiftMapper.map();

        List<Shift> shifts = ConnectionCreationAndUsage.findMany(getAllShiftsQuery, mapper);

        return shifts;
    }

    public void createShift(Shift shift) {
        String insertShiftQuery = "INSERT INTO shifts VALUES (?,?,?,?,?)";
        ConnectionCreationAndUsage.execute(insertShiftQuery, shift.getShiftId(),shift.getStartTime(), shift.getEndTime(),
                shift.getLocation().getLocationId(), shift.getCourseEnum());

    }

    public void updateShift(Shift shift) {
        String updateShiftQuery = "UPDATE shifts SET start_time=?, end_time=?, location_id=?, course_enum=? " +
                "WHERE shift_id=?";
        ConnectionCreationAndUsage.execute(updateShiftQuery, shift.getStartTime(), shift.getEndTime(), shift.getLocation().getLocationId(),
                shift.getCourseEnum(), shift.getShiftId());

    }

    public void deleteShift(UUID shiftId) {
        String deleteShiftQuery = "DELETE FROM shifts WHERE shift_id=?";
        ConnectionCreationAndUsage.execute(deleteShiftQuery, shiftId);

    }
}
