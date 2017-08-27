package com.rqlite.url;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.impl.url.Url;

public class UrlTest {
    @Test
    public void testUrl() {
        Url u = new Url("http://www.foo.com");
        Assert.assertEquals("http://www.foo.com", u.toString());
    }

    @Test
    public void testUrlTransaction() {
        Url u = new Url("http://www.foo.com");

        u.enableTransaction(true);
        Assert.assertEquals("http://www.foo.com?transaction=true", u.toString());

        u.enableTransaction(false);
        Assert.assertEquals("http://www.foo.com", u.toString());
    }

    @Test
    public void testUrlTimings() {
        Url u = new Url("http://www.foo.com");

        u.enableTimings(true);
        Assert.assertEquals("http://www.foo.com?timings=true", u.toString());

        u.enableTimings(false);
        Assert.assertEquals("http://www.foo.com", u.toString());
    }
}
