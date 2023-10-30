# Part 1 - Inconsistent Database

# Inconsistency Without Transactions
As seen in the screenshot below, one of two PreparedStatements were run before
a RuntimeException was generated. This should put the clockedIn database in an inconsistent
state as once a shift is created and inserted, labtechs must be assigned to it.

An error was generated before the labtech for the shift was inserted.
![Sample Image](inconsistentNoTransaction.png)

As described above, let's check the database for the `shifts` table to ensure it is inconsistent.
### shifts table
![Sample Image](shiftTable.png)
Shift was inserted as seen as the record at the bottom. Now let's check the `shifts_labtechs` table.

### shifts_labtech table
As you can see, that second query was never done, so now the `shifts_labtech` table does not have the new shift and the database is inconsistent.
![Sample Image](shifts_labtech.png)

# Inconsistency With Transactions

Now let's try to run the same query with transactions.

Same set of queries as before but this time autocommit has been turned off and 
commit will only happen when the labtech shift insert occurs.

If there is an error then rollback will happen in the catch statement 
This simulates a transaction.
![Sample Image](inconsistentWithTransaction.png)

As expected, the `shifts` table does not have the new record.
![Sample Image](shiftTable2.png)

The `shifts_labtechs` table also does not have the new record.
![Sample Image](shifts_labtech2.png)

Transaction enables consistency by ensuring that commits will not happen unless all changes have been made.

# Part 2

