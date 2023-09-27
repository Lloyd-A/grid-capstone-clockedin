package db_sqlinjection_examples;

import java.sql.*;
import java.util.UUID;

public class PreparedStatementSqlInjection {

    public static void main(String[] args) {
        try (Connection con = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/clocked_in", "myuser", "mypassword")) {
            injectionAttemptWithPreparedStatement(con);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Demonstrates the escaping of characters that are reserved by SQL, protecting against SQL injection.
     * @param con Connection object linking the jvm to the database
     * */
    public static void injectionAttemptWithPreparedStatement(Connection con) {
        String updatePositionSql = "UPDATE users SET first_name = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updatePositionSql)) {
            pstmt.setString(1, "Lloyd'--");
            pstmt.setObject(2, UUID.fromString("b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5"));
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Number of Rows Affected: " + rowsAffected);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
