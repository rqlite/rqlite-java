package com.rqlite.dto;

import com.google.api.client.json.GenericJson;

public class ExecuteRequest extends GenericJson {
    public String[] statements;
}
