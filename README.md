# rqlite-java
[![Circle CI](https://circleci.com/gh/rqlite/rqlite-java/tree/master.svg?style=svg)](https://circleci.com/gh/rqlite/rqlite-java/tree/master) [![Google Group](https://img.shields.io/badge/Google%20Group--blue.svg)](https://groups.google.com/group/rqlite)

_Java client for rqlite, currently in development._

## Quick start
```java
// Get a connection to a rqlite node.
Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);

// Create a table.
rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
System.out.println(rows.toString());

// Insert a record.
rqlite.Execute("INSERT INTO foo(name) VALUES(\"fiona\")");
System.out.println(rows.toString());

// Query all records in the table.
rows = rqlite.Query("SELECT * FROM foo", Rqlite.ReadConsistencyLevel.WEAK);
System.out.println(rows.toString());
```
