# rqlite-java
[![Circle CI](https://circleci.com/gh/rqlite/rqlite-java/tree/master.svg?style=svg)](https://circleci.com/gh/rqlite/rqlite-java/tree/master) [![Google Group](https://img.shields.io/badge/Google%20Group--blue.svg)](https://groups.google.com/group/rqlite)

_Java client for rqlite, currently in development._

## Quick start
```java
// Declare variables.
ExecuteResults results = null;
QueryResults rows = null;

// Get a connection to a rqlite node.
Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);

// Create a table.
results = rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
System.out.println(results.toString());

// Insert a record.
results = rqlite.Execute("INSERT INTO foo(name) VALUES(\"fiona\")");
System.out.println(results.toString());

// Query all records in the table.
rows = rqlite.Query("SELECT * FROM foo", Rqlite.ReadConsistencyLevel.WEAK);
System.out.println(rows.toString());
```
