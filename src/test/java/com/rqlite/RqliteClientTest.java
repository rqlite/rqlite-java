package com.rqlite;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.QueryResults;

public class RqliteClientTest {
    @Test
    public void testRqliteClientSingle() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        ExecuteResults results = null;
        QueryResults rows = null;

        results = rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.results.length);
        Assert.assertEquals(0, results.results[0].lastInsertId);

        results = rqlite.Execute("INSERT INTO foo(name) VALUES(\"fiona\")");
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.results.length);
        Assert.assertEquals(1, results.results[0].lastInsertId);

        rows = rqlite.Query("SELECT * FROM foo", Rqlite.ReadConsistencyLevel.WEAK);
        System.out.println(rows.toString());

        Assert.assertNotNull(rows);
        Assert.assertEquals(1, rows.results.length);
        Assert.assertArrayEquals(new String[] { "id", "name" }, rows.results[0].columns);
        Assert.assertArrayEquals(new String[] { "integer", "text" }, rows.results[0].types);
        Assert.assertEquals(1, rows.results[0].values.length);
        // Assert.assertArrayEquals(new Object[] { 1, "fiona" },
        // rows.results[0].values[0]);
    }

    @Test
    public void testRqliteClientMulti() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        ExecuteResults results = null;
        QueryResults rows = null;

        results = rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
        Assert.assertNotNull(results);
        System.out.println(results.toString());

        String[] s = { "INSERT INTO foo(name) VALUES(\"fiona\")", "INSERT INTO foo(name) VALUES(\"declan\")" };
        results = rqlite.Execute(s, false);
        Assert.assertNotNull(results);
        System.out.println(results.toString());

        String[] q = { "SELECT * FROM foo", "SELECT name FROM foo" };
        rows = rqlite.Query(q, false, Rqlite.ReadConsistencyLevel.WEAK);
        Assert.assertNotNull(rows);
        System.out.println(rows.toString());
    }

    @Test
    public void testRqliteClientSyntax() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        ExecuteResults results = null;
        QueryResults rows = null;

        results = rqlite.Execute("nonsense");
        Assert.assertNotNull(results);
        System.out.println(results.toString());

        rows = rqlite.Query("more nonsense", Rqlite.ReadConsistencyLevel.WEAK);
        Assert.assertNotNull(rows);
        System.out.println(rows.toString());
    }
}
