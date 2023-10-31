package com.clockedIn.side_tasks.Module4Part5;

import com.clockedIn.side_tasks.Connection_Pooling.PooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task3 {
    PooledDataSource dataSource;

    public Task3() {
        this.dataSource = new PooledDataSource();
    }

    public void testIndex() {
        try(
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {

            // Populate users table with 20 million rows of data

            /*PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users(user_id, university_id, first_name, last_name, user_role, phone_num, email) VALUES (?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < 2000000; i++) {
                pstmt.setObject(1, UUID.randomUUID());
                pstmt.setString(2, Integer.toString(i));
                pstmt.setString(3, "FirstName" + i);
                pstmt.setString(4, "LastName" + i);
                pstmt.setString(5, "Role" + i);
                pstmt.setString(6, "Phone" + i);
                pstmt.setString(7, "Email" + i);
                pstmt.addBatch();
                if (i % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();*/

            // Delete index
            stmt.execute("DROP INDEX IF EXISTS idx_university_id");
            // Query without index
            ResultSet rs = stmt.executeQuery("EXPLAIN ANALYZE SELECT * FROM users WHERE university_id = '1000043'");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            long start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT * FROM users WHERE university_id = '1000043'");
            System.out.println("--------------------------------------------------");
            System.out.println("Time taken without index: " + (System.currentTimeMillis() - start));

            // Create index
            stmt.execute("CREATE INDEX idx_university_id ON users(university_id)");
            System.out.println("--------------------------------------------------");
            // Query with index
            rs = stmt.executeQuery("EXPLAIN ANALYZE SELECT * FROM users WHERE university_id = '1000067'");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }System.out.println("--------------------------------------------------");
            start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT * FROM users WHERE university_id = '1000067'");
            System.out.println("Time taken with index: " + (System.currentTimeMillis() - start));

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Task3 task3 = new Task3();
        task3.testIndex();
    }
}
