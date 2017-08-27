package com.rqlite.dto;

import com.google.api.client.json.GenericJson;

public class QueryRequest extends GenericJson {
    public String[] statements;
}
