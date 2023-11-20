package com.clockedIn.shiftservice.util.objectmappers;

import com.clockedIn.shiftservice.model.Course;
import com.clockedIn.shiftservice.model.Shift;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

public class ShiftMapper {

    private ShiftMapper(){}
    /**
     * Provides a function that maps the data from a resultSet object to the constructor of that object and
     * instantiates it.
     * @return Function that maps data from ResultSet object to Shift constructor and creates a Shift object.
     * */
    public static Function<ResultSet, Shift> map(){
        return resultSet -> {
            Shift mappedShift;
            try {
                mappedShift = Shift.builder().shiftId((UUID)resultSet.getObject("shift_id"))
                        .startTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                        .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                        .location(LocationMapper.map().apply(resultSet))
                        .courseEnum((Course)resultSet.getObject("course_enum")).build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return mappedShift;
        };
    }
}
