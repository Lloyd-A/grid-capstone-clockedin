package com.clockedIn.side_tasks.Module4Part5;

import com.clockedIn.side_tasks.Connection_Pooling.PooledDataSource;

import java.sql.*;

public class Task4 {

    PooledDataSource dataSource;

    public Task4() {
        this.dataSource = new PooledDataSource();
    }

    public void testCompoundIndex() {

        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("DROP INDEX IF EXISTS idx_universityid_email");
            // Create compound index on shifts_labtechs table
            stmt.execute("CREATE INDEX idx_universityId_email ON users(university_id, email)");

            ResultSet rs = stmt.executeQuery("EXPLAIN ANALYZE SELECT * FROM users WHERE university_id = '1955563' AND email = 'Email1955563'");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            System.out.println("--------------------------------------------------");
            // Query using both columns of the index
            long start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT * FROM users WHERE university_id = '1955563' AND email = 'Email1955563'");
            System.out.println("Time taken with both columns: " + (System.currentTimeMillis() - start));

            System.out.println("--------------------------------------------------");

            rs = stmt.executeQuery("EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'Email1955565'");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            System.out.println("--------------------------------------------------");
            // Query using only one column of the index
            start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT * FROM users WHERE email = 'Email1955565'");
            System.out.println("Time taken with one column: " + (System.currentTimeMillis() - start));

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Task4 task4 = new Task4();
        task4.testCompoundIndex();
    }
}
