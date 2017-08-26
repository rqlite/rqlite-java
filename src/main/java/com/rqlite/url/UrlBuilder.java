package com.rqlite.url;

import com.google.api.client.http.GenericUrl;

public class UrlBuilder {
    private String proto;
    private String host;
    private Integer port;

    public UrlBuilder(final String proto, final String host, final Integer port) {
        this.proto = proto;
        this.host = host;
        this.port = port;
    }

    public QueryUrl Query(String query) {
        String u = String.format("%s://%s:%d/db/query", this.proto, this.host, this.port);
        return (QueryUrl) new QueryUrl(u).set("q", query);
    }

    public ExecuteUrl Execute(String statement) {
        String u = String.format("%s://%s:%d/db/execute", this.proto, this.host, this.port);
        return new ExecuteUrl(u);
    }

    public GenericUrl Status() {
        String u = String.format("%s://%s:%d/status", this.proto, this.host, this.port);
        return new GenericUrl(u);
    }
}
