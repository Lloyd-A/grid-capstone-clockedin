package db_sqlinjection_examples;

import java.sql.*;

public class StatementSqlInjection {

    public static void main(String[] args) {
        try (Connection con = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/clocked_in", "myuser", "mypassword")) {
            injectionAttemptWithStatement(con);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Uses SQL injection to change the value of first_name for all rows of users table in clocked_in to 'hacker'.
     * @param con Connection object linking the jvm to the database
     * */
    public static void injectionAttemptWithStatement(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String insertSql = "UPDATE users SET first_name = 'hacker' --' WHERE user_id = 1";
            stmt.executeUpdate(insertSql);

            String selectSql = "SELECT * FROM users";
            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("user_id") + "\n" +
                            resultSet.getString("first_name"));
                }

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
