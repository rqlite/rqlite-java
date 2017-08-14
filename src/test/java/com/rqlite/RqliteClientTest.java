package com.rqlite;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.dto.ExecuteResults;

public class RqliteClientTest {
    @Test
    public void testRqliteClientCreateTable() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);

        ExecuteResults results = rqlite.Execute("CREATE TABLE foo (id integer not null primary key, name text)");
        Assert.assertNotNull(results);
    }
}
