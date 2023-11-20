package com.clockedIn.shiftservice.util.objectmappers;

import com.clockedIn.shiftservice.model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

public class LocationMapper {

    private LocationMapper(){}

    /**
     * Provides a function that maps the data from a resultSet object to the constructor of that object and
     * instantiates it.
     * @return Function that maps data from ResultSet object to Location constructor and creates a Location object.
     * */
    public static Function<ResultSet, Location> map(){
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
}
