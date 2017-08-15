package com.rqlite.dto;

import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class ExecuteResults extends GenericJson {
    public static class Result extends GenericJson {
        @Key
        public String error;

        @Key("last_insert_id")
        public Integer lastInsertId;

        @Key("rows_affected")
        public Integer rowsAffected;

        @Key
        public float time;
    }

    @Key("results")
    public List<Result> results;

    @Key
    public float time;
}
