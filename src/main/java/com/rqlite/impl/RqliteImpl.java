package com.rqlite.impl;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rqlite.ExecuteResponse;
import com.rqlite.QueryResults;
import com.rqlite.Rqlite;

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
        Url url = this.urlBuilder.query(q).setReadConsistencyLevel(lvl).enableTransaction(tx);
        HttpRequest request = null;
        HttpResponse response = null;
        QueryResults results = null;

        try {
            request = this.requestFactory.buildGetRequest(url);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        try {
            response = request.execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            results = response.parseAs(QueryResults.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return results;
    }

    public ExecuteResponse Execute(String s) {
        Url url = this.urlBuilder.execute(s);
        return null;

    }

    private class Url extends GenericUrl {
        public Url(String encodedUrl) {
            super(encodedUrl);
        }

        public Url enableTransaction(Boolean tx) {
            if (tx) {
                this.put("transaction", tx.toString());
            } else {
                this.remove("transaction");
            }
            return this;
        }
    }

    private class QueryUrl extends Url {
        public QueryUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public QueryUrl setReadConsistencyLevel(ReadConsistencyLevel lvl) {
            this.put("level", lvl.toString());
            return this;
        }
    }

    private class ExecuteUrl extends Url {
        public ExecuteUrl(String encodedUrl) {
            super(encodedUrl);
        }
    }

    private class UrlBuilder {
        private String proto;
        private String host;
        private Integer port;

        public UrlBuilder(final String proto, final String host, final Integer port) {
            this.proto = proto;
            this.host = host;
            this.port = port;
        }

        public QueryUrl query(String query) {
            String u = String.format("%s://%s:%b/db/query", this.proto, this.host, this.port);
            return new QueryUrl(u);
        }

        public Url execute(String statement) {
            String u = String.format("%s://%s:%b/db/execute", this.proto, this.host, this.port);
            return new ExecuteUrl(u);
        }

    }
}
