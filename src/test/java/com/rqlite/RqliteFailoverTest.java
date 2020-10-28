package com.rqlite;

import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.QueryResults;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class RqliteFailoverTest {
    String rqlitePath;
    Process node2;
    Process node3;
    Process node4;

    @Before
    public void setUp() throws IOException{
        // rqlitePath.config should contain one line with a path to the installation directory of Rqlite
        try {
            rqlitePath = Files.readAllLines(Paths.get("resources/rqlitePath.config")).get(0);
        } catch (IOException e){
            System.err.println(e);
        }

        //Create 3 nodes
        ProcessBuilder pb1 = new ProcessBuilder(rqlitePath + "/./rqlited",
                "-node-id", "node.2",
                "-http-addr", "localhost:4007",
                "-raft-addr", "localhost:4008",
                "target/rqlitenodes/node2." + UUID.randomUUID()) ;
        node2 = pb1.start();

        try {
            Thread.sleep(2000); //Give this node 2 seconds to establish itself as leader
        } catch (InterruptedException e) { }

        ProcessBuilder pb2 = new ProcessBuilder(rqlitePath + "/./rqlited",
                "-node-id", "node.3",
                "-http-addr", "localhost:4003",
                "-raft-addr", "localhost:4004",
                "-join", "localhost:4007",
                "target/rqlitenodes/node3." + UUID.randomUUID());
        node3 = pb2.start();

        ProcessBuilder pb3 = new ProcessBuilder(rqlitePath + "/./rqlited",
                "-node-id", "node.4",
                "-http-addr", "localhost:4005",
                "-raft-addr", "localhost:4006",
                "-join", "localhost:4007",
                "target/rqlitenodes/node4." + UUID.randomUUID());
        node4 = pb3.start();
    }

    @Test
    public void testRqliteFailover() throws IOException {
        //connect to node.2 with config file
        Rqlite rqlite = RqliteFactory.connect("resources/FailoverTest.nodehosts.config");
        ExecuteResults results = null;
        QueryResults rows = null;

        try {
            results = rqlite.Execute("CREATE TABLE baz (id integer not null primary key, name text)");
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.results.length);

            //Connect to node.2 without config file
            Rqlite rqlite2 = RqliteFactory.connect("http", "localhost", 4007);

            //Kill leader node.2
            node2.destroy();

            //see if rqlite with config file has recovered
            results = rqlite.Execute("INSERT INTO baz(name) VALUES(\"fiona\")");
            Assert.assertNotNull(results);
            Assert.assertEquals(1, results.results.length);
            Assert.assertEquals(1, results.results[0].lastInsertId);

            rows = rqlite.Query("SELECT * FROM baz", Rqlite.ReadConsistencyLevel.WEAK);
            Assert.assertNotNull(rows);
            Assert.assertEquals(1, rows.results.length);
            Assert.assertArrayEquals(new String[]{"id", "name"}, rows.results[0].columns);
            Assert.assertArrayEquals(new String[]{"integer", "text"}, rows.results[0].types);
            Assert.assertEquals(1, rows.results[0].values.length);
            Assert.assertArrayEquals(new Object[]{new BigDecimal(1), "fiona"}, rows.results[0].values[0]);

            //rqlite without config file should fail
            try {
                rows = rqlite2.Query("SELECT * FROM baz", Rqlite.ReadConsistencyLevel.WEAK);
                Assert.fail("Expected NodeUnavailableException was not thrown.");
            } catch (NodeUnavailableException e) {}

        } catch (Exception e) {
            Assert.fail("Failed due to an unexpected exception.\n" + e.getMessage());
        }
    }

    @After
    public void tearDown(){
        node3.destroy();
        node4.destroy();
    }
}
