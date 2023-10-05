package util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionCreationAndUsageTest {

    int vin_id=0;
    String make = "";
    String model = "";
    Object[] retrievedValues;
    static String insertIntoCars = "MERGE INTO cars (vin_id, make, model) VALUES (?, ?, ?)";


    @BeforeAll
     static void setUp() throws SQLException, IOException {
        ConnectionCreationAndUsage.setUpDatabaseConnnection();
        // Read the initialization script from the file
        InputStream in = ConnectionCreationAndUsageTest.class.getResourceAsStream("/connection_creation_and_usage_h2/init.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder script = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            script.append(line);
            script.append("\n");
        }
        reader.close();
        ConnectionCreationAndUsage.execute(script.toString());
    }


    //    @Test
//    void tables() throws SQLException {
//        assertTrue(ConnectionCreationAndUsage.tableExists("people"));
//
//    }

    @Test
    @DisplayName("Adds Mercedez Benz to Database With Args")
    void insertsBenzWithArgs() {
        //given
        Object[] args = {4, "Mercedez Benz", "GLE"};


        //when
        ConnectionCreationAndUsage.execute(insertIntoCars, args);

        try(Statement statement = ConnectionCreationAndUsage.getConnection().createStatement()) {
            String selectQuery = "SELECT * FROM cars WHERE vin_id = 4";
            try(ResultSet resultSet = statement.executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    vin_id = resultSet.getInt("vin_id");
                    make = resultSet.getString("make");
                    model = resultSet.getString("model");
                }
                retrievedValues = new Object[]{vin_id, make, model};
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //then
        assertEquals(Arrays.toString(args), Arrays.toString(retrievedValues));
    }

    @Test
    @DisplayName("Adds Nissan Skyline to Database With Consumer")
    void insertSkylineWithConsumer() {
        //given
        Object[] args = {5, "Nissan", "Skyline"};

        Consumer<PreparedStatement> preparedStatementConsumer = (pstmt) -> {
            for (int i = 0; i < args.length; i++) {
                try {
                    pstmt.setObject(i + 1, args[i]);
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        //when
        ConnectionCreationAndUsage.execute(insertIntoCars, preparedStatementConsumer);

        try(Statement statement = ConnectionCreationAndUsage.getConnection().createStatement()) {
            String selectQuery = "SELECT * FROM cars WHERE vin_id = 5";
            try(ResultSet resultSet = statement.executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    vin_id = resultSet.getInt("vin_id");
                    make = resultSet.getString("make");
                    model = resultSet.getString("model");
                }
                retrievedValues = new Object[]{vin_id, make, model};
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //then
        assertEquals(Arrays.toString(args), Arrays.toString(retrievedValues));
    }

    @Test
    @DisplayName("Find a Single Car With Vin Id")
    void findACar() {
        //given
        Map<Integer, Object> expectedObject = new HashMap<>();
        expectedObject.put(1, List.of("Honda", "Integra"));

        Map<Integer, Object> objectsInDB = new HashMap<>();
        String selectQuery = "SELECT * FROM cars WHERE model = ?";

        Object[] args = {"Integra"};

        Function<ResultSet, Object> mapper = resultSet -> {
            try {
                vin_id = resultSet.getInt("vin_id");
                make = resultSet.getString("make");
                model = resultSet.getString("model");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            objectsInDB.put(vin_id, List.of(make, model));
            return objectsInDB;
        };
        //when
        Object actualResult = ConnectionCreationAndUsage.findOne(selectQuery, mapper, args);

        //then
        assertEquals(expectedObject, actualResult);
    }

    @Test
    @DisplayName("Find All Cars With Criteria")
    void findCars() {
        //given
        List<Map<Integer, Object>> expectedObjectList = new ArrayList<>();
        Map<Integer, Object> expectedObject1 = new HashMap<>();
        Map<Integer, Object> expectedObject2 = new HashMap<>();
        expectedObject1.put(1, List.of("Honda", "Integra"));
        expectedObject2.put(2, List.of("Honda", "Civic"));

        expectedObjectList.add(expectedObject1);
        expectedObjectList.add(expectedObject2);

        //Map<Integer, Object> objectsInDB = new HashMap<>();
        String selectQuery = "SELECT * FROM cars WHERE make = ?";

        Object[] args = {"Honda"};

        Function<ResultSet, Object> mapper = resultSet -> {
            try {
                vin_id = resultSet.getInt("vin_id");
                make = resultSet.getString("make");
                model = resultSet.getString("model");
                //Map<Integer, Object> objectsInDB = new HashMap<>();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Map<Integer, Object> objectsInDB = new HashMap<>();
            objectsInDB.put(vin_id, List.of(make, model));
            return objectsInDB;
        };
        //when
        Object actualResult = ConnectionCreationAndUsage.findMany(selectQuery, mapper, args);

        //then
        assertEquals(expectedObjectList, actualResult);
    }
}


