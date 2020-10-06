package com.rqlite.impl;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.rqlite.dto.Pong;

public class PingRequest extends GenericRequest{
    private HttpRequest httpRequest;

    public PingRequest(HttpRequest request) {
        this.httpRequest = request;
    }

    public Pong execute() throws IOException {
        HttpResponse response = this.httpRequest.execute();

        HttpHeaders headers = response.getHeaders();
        String version = headers.getFirstHeaderStringValue("X-Rqlite-Version");

        return new Pong(version);
    }
    

}

