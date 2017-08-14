package com.rqlite;

import org.junit.Assert;
import org.junit.Test;

public class RqliteFactoryTest {

    @Test
    public void testCreateRqliteInstance() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        Assert.assertNotNull(rqlite);
    }

}
