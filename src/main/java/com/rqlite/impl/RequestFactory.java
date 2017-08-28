package com.rqlite.impl;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public class RequestFactory {
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private HttpRequestFactory requestFactory;

    private String proto;
    private String host;
    private Integer port;

    private GenericUrl executeUrl;
    private GenericUrl queryUrl;
    private GenericUrl statusUrl;

    public RequestFactory(final String proto, final String host, final Integer port) {
        this.proto = proto;
        this.host = host;
        this.port = port;

        this.executeUrl = new GenericUrl(String.format("%s://%s:%d/db/execute", this.proto, this.host, this.port));
        this.queryUrl = new GenericUrl(String.format("%s://%s:%d/db/query", this.proto, this.host, this.port));
        this.statusUrl = new GenericUrl(String.format("%s://%s:%d/status", this.proto, this.host, this.port));

        this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });
    }

    public ExecuteRequest buildExecuteRequest(String[] stmts) throws IOException {
        HttpRequest request = this.buildPostRequest(this.executeUrl, stmts);
        return new ExecuteRequest(request);
    }

    public QueryRequest buildQueryRequest(String[] stmts) throws IOException {
        HttpRequest request = this.buildPostRequest(this.queryUrl, stmts);
        return new QueryRequest(request);
    }

    public PingRequest buildPingRequest() throws IOException {
        HttpRequest request = this.requestFactory.buildGetRequest(this.statusUrl);
        return new PingRequest(request);
    }

    private HttpRequest buildPostRequest(GenericUrl url, String[] stmts) throws IOException {
        HttpRequest request = this.requestFactory.buildPostRequest(url, new JsonHttpContent(JSON_FACTORY, stmts));
        return request.setParser(new JsonObjectParser(JSON_FACTORY));
    }
}
