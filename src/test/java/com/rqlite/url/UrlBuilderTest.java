package com.rqlite.url;

import org.junit.Assert;
import org.junit.Test;

public class UrlBuilderTest {
    @Test
    public void testUrlBuilderQuery() {
        UrlBuilder builder = new UrlBuilder("http", "localhost", 4001);
        QueryUrl url = builder.Query("SELECT * FROM foo");
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo", url.toString());
    }

    @Test
    public void testUrlBuilderExecute() {
        UrlBuilder builder = new UrlBuilder("http", "localhost", 4001);
        ExecuteUrl url = builder.Execute("INSERT INTO foo(name) VALUES(1)");
        Assert.assertEquals("http://localhost:4001/db/execute", url.toString());
    }
}
