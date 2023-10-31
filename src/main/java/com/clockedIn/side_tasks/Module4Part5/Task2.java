package com.clockedIn.side_tasks.Module4Part5;

import com.clockedIn.side_tasks.Connection_Pooling.PooledDataSource;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.*;

public class Task2 implements Runnable {
    private String shiftId;
    private String labtechId;
    PooledDataSource dataSource;
    private final CyclicBarrier barrier;

    public Task2(String shiftId, String labtechId, CyclicBarrier barrier) {
        this.shiftId = shiftId;
        this.labtechId = labtechId;
        dataSource = new PooledDataSource();
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try (Connection conn = dataSource.getConnection()) {
            barrier.await();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String functionCall = "{ ? = call ApproveRequest(?, ?) }";
            try (CallableStatement stmt = conn.prepareCall(functionCall);) {
                UUID requestId = UUID.fromString(shiftId);
                UUID actionPerformedBy = UUID.fromString(labtechId);


                stmt.setObject(2, requestId);
                stmt.setObject(3, actionPerformedBy);


                // Register the type of the out parameter
                stmt.registerOutParameter(1, Types.VARCHAR);

                // Execute the function
                stmt.execute();

                // Get the returned message
                String message = stmt.getString(1);

                System.out.println("Returned Message: " + message);

                conn.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {

        // Assuming shiftId and labtechId are retrieved from somewhere
        String shiftId = "123e4567-e89b-12d3-a456-426655440000";
        String[] labtechIds = {"b5092fa6-1c29-4eb1-95c5-7a6d8ec501e5", "b5092fa6-1c29-4eb1-95c8-7a6d8ec501e5", "b5092fa6-1c29-4eb1-95c4-7a6d8ec501e5", "b5092fa6-1c29-4eb1-95c0-7a6d8ec501e5"};
        CyclicBarrier barrier = new CyclicBarrier(labtechIds.length);

        for (String labtechId : labtechIds) {
            Thread worker = new Thread(new Task2(shiftId, labtechId, barrier));
            worker.start();
        }



        System.out.println("Finished all threads");
    }
}

