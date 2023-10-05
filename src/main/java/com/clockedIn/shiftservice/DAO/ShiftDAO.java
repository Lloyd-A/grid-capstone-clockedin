package com.clockedIn.shiftservice.DAO;

import com.clockedIn.shiftservice.Course;
import com.clockedIn.shiftservice.Location;
import com.clockedIn.shiftservice.Shift;
import util.ConnectionCreationAndUsage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class ShiftDAO {

    public Optional<Shift> getShiftById(UUID shiftId) {
        //setDBParameters();

        String getShiftQuery = "SELECT * FROM shifts s JOIN locations l ON s.location_id=l.location_id " +
                "WHERE shift_id = ?";
        Function<ResultSet, Shift> mapper = shiftMapper();

        Shift shift = ConnectionCreationAndUsage.findOne(getShiftQuery, mapper, shiftId);

        return Optional.ofNullable(shift);
    }

    public Optional<Shift> getShiftByCourse(Course course) {
        //setDBParameters();

        String getShiftByCourseQuery = "SELECT * FROM shifts WHERE course_enum = ?";
        Function<ResultSet, Shift> mapper = shiftMapper();

        Shift shift = ConnectionCreationAndUsage.findOne(getShiftByCourseQuery, mapper, course);

        return Optional.ofNullable(shift);
    }

    public Iterable<Shift> getAllShifts() {
        //setDBParameters();
        String getAllShiftsQuery = "SELECT * FROM shifts s JOIN locations l ON s.location_id=l.location_id";
        Function<ResultSet, Shift> mapper = shiftMapper();

        List<Shift> shifts = ConnectionCreationAndUsage.findMany(getAllShiftsQuery, mapper);

        return shifts;
    }

    public void createShift(Shift shift) {
        //setDBParameters();
        String insertShiftQuery = "INSERT INTO shifts VALUES (?,?,?,?,?)";
        ConnectionCreationAndUsage.execute(insertShiftQuery, shift.getShiftId(),shift.getStartTime(), shift.getEndTime(),
                shift.getLocation().getLocationId(), shift.getCourseEnum());

    }

    public void updateShift(Shift shift) {
        //setDBParameters();
        String updateShiftQuery = "UPDATE shifts SET start_time=?, end_time=?, location_id=?, course_enum=? " +
                "WHERE shift_id=?";
        ConnectionCreationAndUsage.execute(updateShiftQuery, shift.getStartTime(), shift.getEndTime(), shift.getLocation().getLocationId(),
                shift.getCourseEnum(), shift.getShiftId());

    }

    public void deleteShift(UUID shiftId) {
        //setDBParameters();
        String deleteShiftQuery = "DELETE FROM shifts WHERE shift_id=?";
        ConnectionCreationAndUsage.execute(deleteShiftQuery, shiftId);

    }

    /**
     * Provides a function that maps the data from a resultSet object to the constructor of that object and
     * instantiates it.
     * @return Function that maps data from ResultSet object to Shift constructor and creates a Shift object.
    * */
    private Function<ResultSet, Shift> shiftMapper(){
        return resultSet -> {
            Shift mappedShift;
            try {
                mappedShift = Shift.builder().shiftId((UUID)resultSet.getObject("shift_id"))
                        .startTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                        .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                        .location(locationMapper().apply(resultSet))
                        .courseEnum((Course)resultSet.getObject("course_enum")).build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return mappedShift;
        };
    }


    /**
     * Provides a function that maps the data from a resultSet object to the constructor of that object and
     * instantiates it.
     * @return Function that maps data from ResultSet object to Location constructor and creates a Location object.
     * */
    private Function<ResultSet, Location> locationMapper(){
        return resultSet -> {
            Location mappedLocation;
            try{
                mappedLocation = Location.builder()
                        .locationId((UUID)resultSet.getObject("location_id"))
                        .roomName(resultSet.getString("room_name"))
                        .virtualLink(resultSet.getString("virtual_link"))
                        .build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return mappedLocation;
        };
    }

    /*private void setDBParameters(){
        ConnectionCreationAndUsage
                .setUpDatabaseConnnection("jdbc:postgresql://localhost:5432/postgres", "myuser", "mypassword");
    }*/
}
