package com.rqlite.dto;

import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class QueryResults extends GenericJson {
    public static class Result extends GenericJson {
        @Key
        public String error;

        @Key
        public List<String> columns;

        @Key
        public List<String> types;

        @Key
        public List<Object> values;

        @Key
        public float time;
    }

    @Key
    public List<Result> results;
}
