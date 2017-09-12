package com.rqlite.dto;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class ExecuteResults extends GenericJson {
    public static class Result extends GenericJson {
        @Key
        public String error;

        @Key("last_insert_id")
        public int lastInsertId;

        @Key("rows_affected")
        public int rowsAffected;

        @Key
        public float time;
    }

    @Key("results")
    public Result[] results;

    @Key
    public float time;
}
