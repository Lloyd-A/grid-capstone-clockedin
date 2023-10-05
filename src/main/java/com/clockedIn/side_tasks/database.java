package com.clockedIn.side_tasks;

import java.sql.*;

public class database {
    public static void main(String[] args) {
        String databaseUrl = "jdbc:postgresql://localhost:10001/testdb";
        String user = "rgilzene";
        String password = "#rgilzene#";


        try (Connection conn = DriverManager.getConnection(databaseUrl, user, password)) {
            try (Statement statement = conn.createStatement()) {
                String userName = "admin"; // user input
                String pass = "admin' OR '1'='1"; // user input
                ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + userName + "' AND password = '" + pass + "'");
                while (rs.next()) {
                    System.out.print("ID is: " + rs.getInt("id"));
                    System.out.print("| Username is: " + rs.getString("username"));
                    System.out.print("| Passwors is: " + rs.getString("password"));
                    System.out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");) {
                String userName = "admin"; // user input
                String pass = "admin' OR '1'='1"; // user input
                pstmt.setString(1, userName);
                pstmt.setString(2, pass);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    System.out.print("ID is: " + rs.getInt("id"));
                    System.out.print("| Username is: " + rs.getString("username"));
                    System.out.print("| Passwors is: " + rs.getString("password"));
                    System.out.println();
                }
            } catch (SQLException e) {
            e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
