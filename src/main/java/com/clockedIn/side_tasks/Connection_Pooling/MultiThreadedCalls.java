package com.clockedIn.side_tasks.Connection_Pooling;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadedCalls {
    public static void main(String[] args) {
        DataSource dsSingle = new SingleDataSourceImpl();
        DataSource dsPooled = new PooledDataSource();

        System.out.println("Ds Pooled Time");
        runThreadedTest(dsPooled);

        System.out.println("Ds Single Time");
        runThreadedTest(dsSingle);
    }

    public static void runThreadedTest(DataSource ds) {
        long startTime = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Connection conn = ds.getConnection();
                    conn.createStatement().execute("SELECT pg_sleep(1)");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threads.add(thread);

        }
        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println(duration);

    }
}
