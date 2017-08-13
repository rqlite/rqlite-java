package com.rqlite;

import java.util.List;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class QueryResults extends GenericJson {
    public class Result {
        @Key
        public List<String> columns;

        @Key
        public List<String> types;

        @Key
        public List<Object> values;

        @Key
        public float time;
    }

    @Key("results")
    public List<Result> results;
}
