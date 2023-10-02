package com.clockedIn.messaround;

import java.sql.*;
import java.util.UUID;

public class TestUseCaseQueries {
    public static void main(String[] args) {
        String dataBaseURl = "jdbc:postgresql://localhost:10000/clocked_in";
        String user = "rgilzene";
        String password = "#rgilzene#";

        try(Connection conn = DriverManager.getConnection(dataBaseURl, user, password)) {
            String functionCall = "{ ? = call RequestShiftSwap(?, ?, ?, ?, ?, ?, ?, ?) }";

            try (CallableStatement stmt = conn.prepareCall(functionCall);) {
                UUID request_id = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
                UUID requestor_id = UUID.fromString("b5092fa6-1c29-4eb1-95c9-7a6d8ec501e5");
                UUID requested_shift = UUID.fromString("a7d74f85-62e6-4c18-9df7-3b91f0a2d6e8");
                UUID proposed_shift = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");

                stmt.setObject(2, request_id);
                stmt.setObject(3, requestor_id);
                stmt.setString(4, "I dont wanna do this anymore");
                stmt.setString(5, "PENDING");
                stmt.setTimestamp(6, Timestamp.valueOf("2023-09-22 10:00:00"));
                stmt.setTimestamp(7, null);
                stmt.setObject(8, requested_shift);
                stmt.setObject(9, proposed_shift);

                // Register the type of the out parameter
                stmt.registerOutParameter(1, Types.VARCHAR);

                // Execute the function
                stmt.execute();

                // Get the returned message
                String message = stmt.getString(1);

                System.out.println("Returned Message: " + message);
            } catch (SQLException m) {
                m.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
