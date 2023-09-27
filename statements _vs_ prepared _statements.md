# Statement vs Prepared Statement

## Overview
The Statement and PreparedStatement interface are both part of the Java Database Connectivity (JDBC), 
which allows Java applications to interact with databases by sending queries, retrieving results and performing
other database-related operations.
It is advised that for both Statement and PreparedStatement, they are handled in a try-with-resources to enable 
automatic resource management.

## Statement
Statements accept strings as queries. For this reason, they are static in nature, i.e, you can not pass parameters to
the query at runtime to alter the conditions of said query. 

### Creation of Statement
To create a <span style="color: green; font-family: Times New Roman; font-size: 18px;">Statement</span> object we use:

````
try (Statement stmt = con.createStatement()) {
    // use stmt here
}
````
where con is the connection to the database created by using a DriverManager.

### Execution of SQL with Statement
When using Statements, there are three methods to execute SQL queries:

| Method          | Query type                                      |
|:----------------|:------------------------------------------------|
| executeQuery()  | SELECT queries                                  |
| executeUpdate() | Database altering queries (UPDATE, INSERT, etc) |
| execute()       | SELECT and UPDATE when result is unknown        |

Just so we have a consistent schema to work with, here is how to create a table to the database using the 
execute() method:
````
String tableSql = "CREATE TABLE IF NOT EXISTS cars (vin_id int PRIMARY KEY AUTO_INCREMENT, make varchar(30),"
  + "model varchar(30), year int)";
stmt.execute(tableSql);
````

Now, here is how to update the table with values:
````
String insertSql = "INSERT INTO cars(make, model, mileage) VALUES('Honda', 'Integra', 2002)";
stmt.executeUpdate(insertSql);
````

## PreparedStatement

PreparedStatement objects contain precompiled SQL sequences having one or more parameters denoted as question marks.
This enables their dynamic behaviour allowing arguments to be passed, i.e., SQL queries to be made, at runtime.

### Creation of Statement
To create a <span style="color: green; font-family: Times New Roman; font-size: 18px;">PreparedStatement</span> object we use:
````
String updatePositionSql = "UPDATE cars SET model=? WHERE vin_id=?";
try (PreparedStatement pstmt = con.prepareStatement(updatePositionSql)) {
    // use pstmt here
}
````

### Execution of SQL with PreparedStatement
To execute an SQL query with a PreparedStatement, setters are used in the format set(x, y) where x is the order of the parameter
and y is the value of the argument to be passed:
1. pstmt.setString(1, "Civic");
2. pstmt.setInt(2, 1);

One of the aforementioned methods are used for the execution of the query. In this case, executeUpdate():
````
int rowsAffected = pstmt.executeUpdate();
````

## Main Differences Between Statement and PreparedStatement

1. Statements accept strings as sql queries which make them less readable when concatenated while PrepareStatements use 
precompiled SQL sequences that have arguments passed to them at runtime. PreparedStatements also has methods to bind
object types. This was seen with the setters in execution with PreparedStatements, binding to string and integer.
2. Statements are vulnerable to SQL injection because certain characters will not be escaped. For example:
   - Updating a cars' model using a Car object's attributes and the car's model is "Accord' --". By placing this in 
   an UPDATE query the characters '--' will be seen as the comment delimiter in SQL and any conditions will be commented
   making all cars in the database have mode Accord.
   
   UPDATE cars SET model = 'Accord' <span style="color: grey;"> -- WHERE id = 1;</span>
PreparedStatements protect against this by appropriately escaping characters for all arguments provided.
3. Because PreparedStatements are precompiled, the database engine will cache a query that has been made and as a result
optimize the process of the jvm communicating with the database and the query being executed. With Statements, the database engine
will need to do all checks on each query to ensure their validity.
