# Connection Pooling in Java
Connection pooling is a well-known data access pattern. Its main purpose is to reduce the overhead involved in performing database connections and read/write database operations. At the most basic level, a connection pool is a database connection cache implementation that can be configured to suit specific requirements.


# Why Connection Pooling?
Database connections are fairly expensive operations, and as such, should be reduced to a minimum in every possible use case. Connection pooling implementations come into play by implementing a database connection container, which allows us to reuse a number of existing connections. This can effectively save the cost of performing a huge number of expensive database trips, boosting the overall performance of our database-driven applications.


# Advantages of Connection Pooling
- Performance Improvement: Reusing existing connections rather than creating new ones each time a connection is requested, as establishing a connection is resource-intensive and time-consuming.
- Effective Resource Utilization: Connection pooling allows applications to have as many open connections as needed up to the maximum pool size. This helps in effective utilization of database resources.
- Better Application Responsiveness: Connection pooling can improve the response time of your application by reusing existing connections.


# Disadvantages of Connection Pooling
- Complexity: Implementing connection pooling increases the complexity of the application.
- Resource Management: If not managed properly, connection pools can lead to resource leakage. For example, if connections are not closed properly, it may lead to exhaustion of the pool.
- Stale Connections: There might be scenarios where the physical database connection is lost (like DB restart, network issues), but the connection pool is unaware of this. It still holds that connection object which is stale now.


# Implications of Using a single Datasource vs Connection Pooling
- As expected, when using a single datasource object and 10 threads trying to make calls to the database using pg_sleep(1), there was a somewhat queue, in terms of waiting to acquire the database object to make their calls.
- With connection pooling on the other-hand, there is a sort of flexibility as the pool can create more datasource objects as needed plus save the already created ones for future use.
- The single datasource scenario ran for 10s as each thread, total 10, made a 1s call to the datasource object.
- The connection pool approach took a total of 1s for all 10 thread calls as it saved, stored and adapted to the needs of each thread by creating and reusing datasource objects.