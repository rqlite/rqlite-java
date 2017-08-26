package com.rqlite.impl.url;

import com.rqlite.Rqlite.ReadConsistencyLevel;

public class QueryUrl extends Url {
    public QueryUrl(String encodedUrl) {
        super(encodedUrl);
    }

    public QueryUrl setReadConsistencyLevel(ReadConsistencyLevel lvl) {
        this.put("level", lvl.toString());
        return this;
    }
}
