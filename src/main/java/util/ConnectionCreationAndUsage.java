package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ConnectionCreationAndUsage {

    public static Connection connection;
    private static String url;
    private static String username;
    private static String password;

    /**
     * Constructor is private to ensure the class cannot be instantiated as it is a utility class.
     * */
    private ConnectionCreationAndUsage(){}

    /**
     * Sets the attribute fields necessary to establish a connection to a database
     * @param url Url to the database with which a connection is desired.
     * @param username Username required to access the database.
     * @param password Password required to access the database.
     * */
    public static void setUpDatabaseConnnection(String url, String username, String password) {
        ConnectionCreationAndUsage.url = url;
        ConnectionCreationAndUsage.username = username;
        ConnectionCreationAndUsage.password = password;
    }

    /**
     * Establishes a connection with a database using the attributes url, username,
     * and password via DriverManager.getConnection(String url, String username, String password). The established
     * connection is then stored in the attribute connection.
     * */
    private static void establishConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Closes the connection held by attribute connection.
     * */
    public static void closeConnection() {
        try{
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a PreparedStatement object and passes arguments to it using its setObject() method. The PreparedStatement
     * object, now having all arguments, is executed against the database.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param args Array holding all the arguments to be used as replacements for all occurrences of '?' in the
     *             precompiled PreparedStatement object.
     * */
    public static void execute(String query, Object... args) {
        establishConnection();
        try (PreparedStatement preparedstatement = connection.prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                preparedstatement.setObject(i + 1, args[i]);
            }
            preparedstatement.execute();
        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
        }
    }

    /**
     * Creates a PreparedStatement object and passes it to a consumer to set its arguments. The PreparedStatement
     * object, now having all arguments, is executed against the database.
     * @param query String holding the required query with placeholders denoted by '?' to be precompiled.
     * @param preparedStatementConsumer Consumer used to replace all '?' in the PreparedStatement object with arguments.
     * */
    public static void execute(String query, Consumer<PreparedStatement> preparedStatementConsumer) {
        establishConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatementConsumer.accept(preparedStatement);
            preparedStatement.execute();
        }
        catch (SQLException e) {
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
        establishConnection();
        try (PreparedStatement preparedstatement = connection.prepareStatement(query)) {
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
        }
        catch (SQLException e) {
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
        establishConnection();
        List<T> result = new ArrayList<>();
        establishConnection();
        try (PreparedStatement preparedstatement = connection.prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                preparedstatement.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = preparedstatement.executeQuery();
            while (resultSet.next()) {
                result.add(mapper.apply(resultSet));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;

    }

    public static String getSchema() throws SQLException {
        establishConnection();
        return connection.getSchema();
    }
    public static boolean tableExists(String tableName) throws  SQLException {
        establishConnection();

        // Check if the specified table exists in the database
        return connection.getMetaData().getTables(null, null, tableName, null).next();
    }
}
