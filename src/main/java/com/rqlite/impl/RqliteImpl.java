package com.rqlite.impl;

import java.io.IOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rqlite.Rqlite;
import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.Pong;
import com.rqlite.dto.QueryResults;

public class RqliteImpl implements Rqlite {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final RequestFactory requestFactory;

    public RqliteImpl(final String proto, final String host, final Integer port) {
        this.requestFactory = new RequestFactory(proto, host, port);
    }

    public QueryResults Query(String[] stmts, boolean tx, ReadConsistencyLevel lvl) {
        QueryRequest request;

        try {
            request = this.requestFactory.buildQueryRequest(stmts);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        request.enableTransaction(tx).setReadConsistencyLevel(lvl);

        try {
            return request.execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public QueryResults Query(String s, ReadConsistencyLevel lvl) {
        return this.Query(new String[] { s }, false, lvl);
    }

    public ExecuteResults Execute(String[] stmts, boolean tx) {
        ExecuteRequest request;

        try {
            request = this.requestFactory.buildExecuteRequest(stmts);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        request.enableTransaction(tx);

        try {
            return request.execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public ExecuteResults Execute(String s) {
        return this.Execute(new String[] { s }, false);
    }

    public Pong Ping() {
        try {
            return this.requestFactory.buildPingRequest().execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
