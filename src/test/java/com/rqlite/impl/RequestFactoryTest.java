package com.rqlite.impl;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.Rqlite;

public class RequestFactoryTest {
    @Test
    public void testUrlBuilderQuery() throws IOException {
        RequestFactory factory = new RequestFactory("http", "localhost", 4001);
        QueryRequest request = factory.buildQueryRequest(null);
        Assert.assertEquals("http://localhost:4001/db/query", request.getUrl());

        request.enableTransaction(true);
        Assert.assertEquals("http://localhost:4001/db/query?transaction=true", request.getUrl());

        request.enableTransaction(false);
        Assert.assertEquals("http://localhost:4001/db/query", request.getUrl());
    }

    @Test
    public void testUrlBuilderQueryStatement() {
    }

    @Test
    public void testUrlBuilderQueryReadConsistency() throws IOException {
        RequestFactory factory = new RequestFactory("http", "localhost", 4001);
        QueryRequest request = factory.buildQueryRequest(null);
        Assert.assertEquals("http://localhost:4001/db/query", request.getUrl());

        request.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.STRONG);
        Assert.assertEquals("http://localhost:4001/db/query?level=strong", request.getUrl());

        request.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.WEAK);
        Assert.assertEquals("http://localhost:4001/db/query?level=weak", request.getUrl());

        request.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.NONE);
        Assert.assertEquals("http://localhost:4001/db/query?level=none", request.getUrl());
    }

    @Test
    public void testUrlBuilderExecute() throws IOException {
        RequestFactory factory = new RequestFactory("http", "localhost", 4001);
        ExecuteRequest request = factory.buildExecuteRequest(null);
        Assert.assertEquals("http://localhost:4001/db/execute", request.getUrl());

        request.enableTransaction(true);
        Assert.assertEquals("http://localhost:4001/db/execute?transaction=true", request.getUrl());

        request.enableTransaction(false);
        Assert.assertEquals("http://localhost:4001/db/execute", request.getUrl());
    }
}
