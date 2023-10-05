# DriverManager vs Datasource for Java Database Connections

## Overview
DriverManager is a class in java which is used to provide a service to users which wish to make a connection to their respective databases. It provides a basic service for managing a set of JDBC drivers. It keeps track of the drivers that are available and handles establishing a connection between a database and the appropriate driver.

*IN CONTRAST*

On the other hand, DataSource is an interface in the javax.sql package3. It represents a factory for connections to the physical data source that this DataSource object represents3. An object that implements the DataSource interface will typically be registered with a naming service based on the Java™ Naming and Directory (JNDI) API3.


## DriverManager

### Advantages
- DriverManager is a basic service for managing a set of JDBC drivers.
- It keeps track of the drivers that are available and handles establishing a connection between a database and the appropriate driver.
- It allows a user to customize the JDBC Drivers used by their applications.

### Disadvantages
- DriverManager hampers the application performance as the connections are created/closed in java classes.
- It does not support connection pooling.
- For DriverManager, you need to know all the details (host, port, username, password, driver class) to connect to DB and to get connections.


## Datasource

### Advantages
- DataSource is an interface in the javax.sql package. It represents a factory for connections to the physical data source that this DataSource object represents.
- It allows details about the underlying data source to be transparent to the application.
- Using a DataSource you only need to know the JNDI name. The AppServer cares about the details and is not configured by the client application’s vendor, but by an admin where the application is hosted.
- DataSource improves application performance as connections are not created/closed within a class, they are managed by the application server and can be fetched while at runtime.
- It provides a facility creating a pool of connections helpful for enterprise applications

### Disadvantages
- The return value for the method lookup is a reference to a Java, the most generic of objects, so it must be cast to the more narrow DataSource before it can be assigned to the DataSource variable ds.
