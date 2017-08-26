package com.rqlite.impl;

import java.io.IOException;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rqlite.Rqlite;
import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.Pong;
import com.rqlite.dto.QueryResults;
import com.rqlite.url.Url;
import com.rqlite.url.UrlBuilder;

public class RqliteImpl implements Rqlite {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final HttpRequestFactory requestFactory;
    private final UrlBuilder urlBuilder;

    public RqliteImpl(final String proto, final String host, final Integer port) {
        this.urlBuilder = new UrlBuilder(proto, host, port);

        this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });
    }

    public QueryResults Query(String q, boolean tx, ReadConsistencyLevel lvl) {
        Url url = this.urlBuilder.Query(q).setReadConsistencyLevel(lvl).enableTransaction(tx);
        HttpRequest request = null;
        HttpResponse response = null;
        QueryResults results = null;

        try {
            request = this.requestFactory.buildGetRequest(url);
            response = request.execute();
            results = response.parseAs(QueryResults.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return results;
    }

    public ExecuteResults Execute(String s) {
        Url url = this.urlBuilder.Execute(s);
        HttpRequest request = null;
        HttpResponse response = null;
        ExecuteResults results = null;

        String[] stmts = { s };
        try {
            request = this.requestFactory.buildPostRequest(url, new JsonHttpContent(JSON_FACTORY, stmts));
            request.setParser(new JsonObjectParser(JSON_FACTORY));
            response = request.execute();
            results = response.parseAs(ExecuteResults.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return results;
    }

    public Pong Ping() {
        HttpRequest request = null;
        HttpResponse response = null;

        try {
            request = this.requestFactory.buildGetRequest(this.urlBuilder.Status());
            response = request.execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        HttpHeaders headers = response.getHeaders();
        String version = headers.getFirstHeaderStringValue("X-Rqlite-Version");
        // TODO If version is null, raise.

        return new Pong(version);
    }
}
