package com.rqlite.impl.url;

import com.google.api.client.http.GenericUrl;

public class UrlBuilder {
    private String proto;
    private String host;
    private Integer port;

    private String executeRootUrl;
    private String queryRootUrl;
    private String statusUrl;

    public UrlBuilder(final String proto, final String host, final Integer port) {
        this.proto = proto;
        this.host = host;
        this.port = port;

        this.executeRootUrl = String.format("%s://%s:%d/db/execute", this.proto, this.host, this.port);
        this.queryRootUrl = String.format("%s://%s:%d/db/query", this.proto, this.host, this.port);
        this.statusUrl = String.format("%s://%s:%d/status", this.proto, this.host, this.port);
    }

    public ExecuteUrl Execute() {
        return new ExecuteUrl(this.executeRootUrl);
    }

    public QueryUrl Query() {
        return new QueryUrl(this.queryRootUrl);
    }

    public QueryUrl Query(String query) {
        return (QueryUrl) new QueryUrl(this.queryRootUrl).set("q", query);
    }

    public GenericUrl Status() {
        return new GenericUrl(this.statusUrl);
    }
}
