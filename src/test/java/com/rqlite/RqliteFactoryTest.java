package com.rqlite;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.rqlite.dto.Pong;

public class RqliteFactoryTest {

    @Test
    public void testCreateRqliteInstance() {
        Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
        Assert.assertNotNull(rqlite);
    }

   @Test
   public void testCreateRqliteInstancePing() {
       Rqlite rqlite = RqliteFactory.connect("http", "localhost", 4001);
      Pong pong = rqlite.Ping();
      Assert.assertEquals(getRqliteVersion(), pong.version);
   }

   private String getRqliteVersion() {
       Map<String, String> getenv = System.getenv();
       if (getenv.containsKey("RQLITE_VERSION")) {
           return getenv.get("RQLITE_VERSION");
       }
       return "unknown";
   }
}
