package com.rqlite.impl;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rqlite.Rqlite;

public class RqliteImpl implements Rqlite {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final HttpRequestFactory requestFactory;
    private final String url;

    public RqliteImpl(final String host, final Integer port) {
        this.url = "http://" + host + ":" + port;

        this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });
    }

    public void Query(String q, boolean tx, ReadConsistencyLevel lvl) {
        HttpRequest request = this.requestFactory.buildGetRequest("xxx"); // XXX needs inherit from generic URL
        // TODO Auto-generated method stub

    }

    public void Execute(String q) {
        // TODO Auto-generated method stub

    }

}
