package com.clockedIn.side_tasks.Module4Part5;

import com.clockedIn.side_tasks.Connection_Pooling.PooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class Tasks {
    PooledDataSource dataSource;

    public Tasks() {
        this.dataSource = new PooledDataSource();
    }

    public void shiftInsertNoTransaction() {

        try (PreparedStatement insertShiftStmt = dataSource.getConnection().prepareStatement("INSERT INTO shifts (shift_id, start_time, end_time, location_id, course_enum) VALUES (?, ?, ?, ?, ?)")) {

            UUID shiftId = UUID.fromString("d14e144b-ec9e-43bb-9b1d-cc9c6a9d9c61");
            UUID locationId = UUID.fromString("1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f");
            insertShiftStmt.setObject(1, shiftId);
            insertShiftStmt.setTimestamp(2, Timestamp.valueOf("2023-01-01 09:00:00"));
            insertShiftStmt.setTimestamp(3, Timestamp.valueOf("2023-01-01 10:00:00"));
            insertShiftStmt.setObject(4, locationId);
            insertShiftStmt.setString(5, "COMP1161");
            insertShiftStmt.executeUpdate();

            // Simulate a system failure here by throwing an exception
            if (true) throw new RuntimeException("System failure!");

            try (PreparedStatement insertShiftLabTechStmt = dataSource.getConnection().prepareStatement("INSERT INTO shifts_labtechs (shift_id, user_id) VALUES (?, ?)")) {
                UUID userId = UUID.fromString("b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5");
                insertShiftLabTechStmt.setObject(1, shiftId);
                insertShiftLabTechStmt.setObject(2, userId);
                insertShiftLabTechStmt.executeUpdate();
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void shiftInsertWithTransaction() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertShiftStmt = connection.prepareStatement("INSERT INTO shifts (shift_id, start_time, end_time, location_id, course_enum) VALUES (?, ?, ?, ?, ?)")) {

            connection.setAutoCommit(false);
            UUID shiftId = UUID.fromString("d14e144b-ec9e-43bb-9b1d-cc9c6a9d9c61");
            UUID locationId = UUID.fromString("1f2e6d3c-4a5b-6c7d-8e9f-0a1b2c3d4e5f");
            insertShiftStmt.setObject(1, shiftId);
            insertShiftStmt.setTimestamp(2, Timestamp.valueOf("2023-01-01 09:00:00"));
            insertShiftStmt.setTimestamp(3, Timestamp.valueOf("2023-01-01 10:00:00"));
            insertShiftStmt.setObject(4, locationId);
            insertShiftStmt.setString(5, "COMP1161");
            insertShiftStmt.executeUpdate();

            // Simulate a system failure here by throwing an exception
            if (true) throw new RuntimeException("System failure!");

            try (PreparedStatement insertShiftLabTechStmt = dataSource.getConnection().prepareStatement("INSERT INTO shifts_labtechs (shift_id, user_id) VALUES (?, ?)")) {
                UUID userId = UUID.fromString("b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5");
                insertShiftLabTechStmt.setObject(1, shiftId);
                insertShiftLabTechStmt.setObject(2, userId);
                insertShiftLabTechStmt.executeUpdate();

                connection.commit();
            } catch (SQLException sqle) {
                connection.rollback();
                System.out.println(sqle.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

    }
    public static void main(String[] args) {
        Tasks tasks = new Tasks();
        tasks.shiftInsertNoTransaction();
        tasks.shiftInsertWithTransaction();
    }
}
