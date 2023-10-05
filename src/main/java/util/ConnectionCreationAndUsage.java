package util;

import com.clockedIn.side_tasks.Connection_Pooling.PooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ConnectionCreationAndUsage {


    private static DataSource ds;

    /**
     * Constructor is private to ensure the class cannot be instantiated as it is a utility class.
     * */
    private ConnectionCreationAndUsage(){}

    public static DataSource getDs() {
        return ds;
    }

    /**
     * Sets the attribute fields necessary to establish a connection to a database
     * */
    public static synchronized void setUpDatabaseConnnection() {
        HikariConfig config = new HikariConfig();
        Properties props = new Properties();

        try {
            props.load(PooledDataSource.class.getResourceAsStream("/dbdb.properties"));
            String databaseURL = props.getProperty("dbURL");
            String databaseUsername = props.getProperty("dbUser");
            String databasePassword = props.getProperty("dbPassword");
            config.setJdbcUrl(databaseURL);
            config.setUsername(databaseUsername);
            config.setPassword(databasePassword);

            ds = new HikariDataSource(config);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Establishes a connection with a database using the attributes url, username,
     * and password via DriverManager.getConnection(String url, String username, String password). The established
     * connection is then stored in the attribute connection.
     * */
    public static Connection getConnection() {
        if (ds == null) {
            setUpDatabaseConnnection();
        }
        try {
            return getDs().getConnection();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }



    /**
     * Creates a PreparedStatement object and passes arguments to it using its setObject() method. The PreparedStatement
     * object, now having all arguments, is executed against the database.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param args Array holding all the arguments to be used as replacements for all occurrences of '?' in the
     *             precompiled PreparedStatement object.
     * */
    public static void execute(String query, Object... args) {
        try (Connection currentConnection = getConnection()) {
            if (currentConnection != null) {
                try (PreparedStatement preparedstatement = currentConnection.prepareStatement(query)) {
                    for (int i = 0; i < args.length; i++) {
                        preparedstatement.setObject(i + 1, args[i]);
                    }
                    preparedstatement.execute();
                } catch (SQLException e) {
                    System.out.println("SQL Exception");
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    /**
     * Creates a PreparedStatement object and passes it to a consumer to set its arguments. The PreparedStatement
     * object, now having all arguments, is executed against the database.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param preparedStatementConsumer Consumer used to replace all '?' in the PreparedStatement object with arguments.
     * */
    public static void execute(String query, Consumer<PreparedStatement> preparedStatementConsumer) {
        try (Connection currentConnection = getConnection()) {
            if (currentConnection != null) {
                try (PreparedStatement preparedStatement = currentConnection.prepareStatement(query)) {
                    preparedStatementConsumer.accept(preparedStatement);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a PreparedStatement object and passes arguments to it using its setObject() method. The ResultSet
     * is retrieved from the PreparedStatement object and a mapper is used to extract a single result. If the number
     * of extracted results is 0 then the user is notified and the connection is closed. If the number of extracted
     * results is  greater than 1 then a RuntimeException is thrown.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param mapper Function that takes a ResultSet, extracts the cells of the row the cursor is currently at, creates
     *               and returns an object of type T using the extracted data.
     * @param args Array holding all the arguments to be used as replacements for all occurrences of '?' in the
     *             precompiled PreparedStatement object.
     * @return Single entity returned from database as an object
     * */
    public static <T> T findOne(String query, Function<ResultSet, T> mapper, Object... args) {
        T result = null;
            try (Connection currentConnection = getConnection()) {
                if (currentConnection != null) {
                    try (PreparedStatement preparedstatement = currentConnection.prepareStatement(query)) {
                        for (int i = 0; i < args.length; i++) {
                            preparedstatement.setObject(i + 1, args[i]);
                        }
                        ResultSet resultSet = preparedstatement.executeQuery();
                        if (resultSet.next()) {
                            result = mapper.apply(resultSet);
                            if (resultSet.next()) {
                                throw new RuntimeException("ResultSet returned more than one results for query");
                            }
                        } else {
                            System.out.println("Nothing in ResultSet");
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
        return result;
    }

    /**
     * Creates a PreparedStatement object and passes arguments to it using its setObject() method. The ResultSet
     * is retrieved from the PreparedStatement object and a mapper is used to extract a single result.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param mapper Function that takes a ResultSet, extracts the cells of the row the cursor is currently at, creates
     *               and returns an object of type T using the extracted data.
     * @param args Array holding all the arguments to be used as replacements for all occurrences of '?' in the
     *             precompiled PreparedStatement object.
     * @return List of entities returned from database as objects
     * */
    public static <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        List<T> result = new ArrayList<>();
        try (Connection currentConnection = getConnection()) {
            if (currentConnection != null) {
                try (PreparedStatement preparedstatement = currentConnection.prepareStatement(query)) {
                    for (int i = 0; i < args.length; i++) {
                        preparedstatement.setObject(i + 1, args[i]);
                    }
                    ResultSet resultSet = preparedstatement.executeQuery();
                    while (resultSet.next()) {
                        result.add(mapper.apply(resultSet));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return result;

    }


}
