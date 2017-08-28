package com.rqlite.dto;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class QueryResults extends GenericJson {
    public static class Result extends GenericJson {
        @Key
        public String error;

        @Key
        public String[] columns;

        @Key
        public String[] types;

        @Key
        public Object[][] values;

        @Key
        public float time;
    }

    @Key
    public Result[] results;
}
