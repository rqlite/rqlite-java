package com.rqlite.impl;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.rqlite.dto.GenericResults;

import java.io.IOException;

abstract class GenericRequest {

    private HttpRequest httpRequest;

    protected abstract GenericResults execute() throws IOException;
    public String getUrl() {
        return this.httpRequest.getUrl().toString();
    }
    public void setUrl(GenericUrl url){
        this.httpRequest.setUrl(url);
    }
}
