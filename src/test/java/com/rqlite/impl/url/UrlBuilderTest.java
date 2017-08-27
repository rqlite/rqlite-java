package com.rqlite.impl.url;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.Rqlite;
import com.rqlite.impl.url.ExecuteUrl;
import com.rqlite.impl.url.QueryUrl;
import com.rqlite.impl.url.UrlBuilder;

public class UrlBuilderTest {
    @Test
    public void testUrlBuilderQuery() {
        UrlBuilder builder = new UrlBuilder("http", "localhost", 4001);
        QueryUrl url = builder.Query("SELECT * FROM foo");
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo", url.toString());

        url.enableTransaction(true);
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo&transaction=true",
                url.toString());

        url.enableTransaction(false);
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo", url.toString());
    }

    @Test
    public void testUrlBuilderQueryReadConsistency() {
        UrlBuilder builder = new UrlBuilder("http", "localhost", 4001);
        QueryUrl url = builder.Query("SELECT * FROM foo");

        url.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.STRONG);
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo&level=strong", url.toString());

        url.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.WEAK);
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo&level=weak", url.toString());

        url.setReadConsistencyLevel(Rqlite.ReadConsistencyLevel.NONE);
        Assert.assertEquals("http://localhost:4001/db/query?q=SELECT%20*%20FROM%20foo&level=none", url.toString());
    }

    @Test
    public void testUrlBuilderExecute() {
        UrlBuilder builder = new UrlBuilder("http", "localhost", 4001);
        ExecuteUrl url = builder.Execute("INSERT INTO foo(name) VALUES(1)");
        Assert.assertEquals("http://localhost:4001/db/execute", url.toString());

        url.enableTransaction(true);
        Assert.assertEquals("http://localhost:4001/db/execute?transaction=true", url.toString());

        url.enableTransaction(false);
        Assert.assertEquals("http://localhost:4001/db/execute", url.toString());
    }
}
