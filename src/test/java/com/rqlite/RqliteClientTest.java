package com.rqlite;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.QueryResults;

public class RqliteClientTest {
    @Test
    public void testRqliteClient() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        ExecuteResults results = null;
        QueryResults rows = null;

        results = rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
        Assert.assertNotNull(results);
        System.out.println(results.toString());

        results = rqlite.Execute("INSERT INTO foo(name) VALUES(\"fiona\")");
        Assert.assertNotNull(results);
        System.out.println(results.toString());

        rows = rqlite.Query("SELECT * FROM foo", false, Rqlite.ReadConsistencyLevel.WEAK);
        Assert.assertNotNull(rows);
        System.out.println(rows.toString());
    }
}
