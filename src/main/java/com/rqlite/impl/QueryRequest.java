package com.rqlite.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.rqlite.Rqlite.ReadConsistencyLevel;
import com.rqlite.dto.QueryResults;

public class QueryRequest {

    private HttpRequest httpRequest;

    public QueryRequest(HttpRequest request) {
        this.httpRequest = request;
    }

    public QueryResults execute() throws IOException {
        HttpResponse response = this.httpRequest.execute();
        return response.parseAs(QueryResults.class);
    }

    public String getUrl() {
        return this.httpRequest.getUrl().toString();
    }

    public String getMethod() {
        return this.httpRequest.getRequestMethod();
    }

    public String getBody() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.httpRequest.getContent().writeTo(stream);
        return stream.toString();
    }

    public void writeContentTo(OutputStream out) {
        return;
    }

    public QueryRequest setReadConsistencyLevel(ReadConsistencyLevel lvl) {
        this.httpRequest.getUrl().put("level", lvl.toString().toLowerCase());
        return this;
    }

    public QueryRequest enableTransaction(Boolean tx) {
        if (tx) {
            this.httpRequest.getUrl().put("transaction", tx.toString());
        } else {
            this.httpRequest.getUrl().remove("transaction");
        }
        return this;
    }

    public QueryRequest enableTimings(Boolean tm) {
        if (tm) {
            this.httpRequest.getUrl().put("timings", tm.toString());
        } else {
            this.httpRequest.getUrl().remove("timings");
        }
        return this;
    }
}