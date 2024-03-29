package com.clockedIn.side_tasks.Connection_Pooling;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class SingleDataSourceImpl implements DataSource {
    private Connection connection;

    public SingleDataSourceImpl() {
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        synchronized (this) {
            if (connection == null) {
                Properties props = new Properties();
                try {
                    props.load(SingleDataSourceImpl.class.getResourceAsStream("/application-dev.properties"));
                    String databaseURL = props.getProperty("dbURL");
                    String databaseUsername = props.getProperty("dbUser");
                    String databasePassword = props.getProperty("dbPassword");
                    connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
                    System.out.println("Initialized");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
