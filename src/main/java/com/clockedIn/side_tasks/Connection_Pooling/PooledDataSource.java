package com.clockedIn.side_tasks.Connection_Pooling;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource{

    DataSource ds;
    public PooledDataSource() {
        HikariConfig config = new HikariConfig();

        Properties props = new Properties();
        try {
            props.load(PooledDataSource.class.getResourceAsStream("/database.properties"));
            String databaseURL = props.getProperty("dbURL");
            String databaseUsername = props.getProperty("dbUser");
            String databasePassword = props.getProperty("dbPassword");
            config.setJdbcUrl(databaseURL);
            config.setUsername(databaseUsername);
            config.setPassword(databasePassword);

            this.ds = new HikariDataSource(config);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
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
