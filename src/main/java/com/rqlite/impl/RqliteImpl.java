package com.rqlite.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rqlite.NodeUnavailableException;
import com.rqlite.Rqlite;
import com.rqlite.dto.ExecuteResults;
import com.rqlite.dto.GenericResults;
import com.rqlite.dto.ParameterizedStatement;
import com.rqlite.dto.Pong;
import com.rqlite.dto.QueryResults;

public class RqliteImpl implements Rqlite {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private RequestFactory requestFactory;

    private List<RqliteNode> peers; // only initialized if evaluating a config file
    private int timeoutDelay = 8000;

    Map<RqliteNode, RequestFactory> nodeRequestFactoryMap = new HashMap<>();

    public RqliteImpl(final String proto, final String host, final Integer port) {
        this.requestFactory = new RequestFactory(proto, host, port);
    }

    public RqliteImpl(final String configPath) {
        loadPeersFromConfig(configPath);
        this.requestFactory = new RequestFactory(peers.get(0).proto, peers.get(0).host, peers.get(0).port);
    }

    public void setTimeoutDelay(int delay) {
        this.timeoutDelay = delay;
    }

    private void loadPeersFromConfig(String configPath){
        this.peers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                peers.add(new RqliteNode(values[0], values[1], Integer.valueOf(values[2])));
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GenericResults tryOtherPeers(GenericRequest request, String[] stmts) throws NodeUnavailableException {
        // Cycle through the list of nodes in the config file.
        long end = System.currentTimeMillis() + timeoutDelay;
        if (peers != null) {
            while (System.currentTimeMillis() < end) {
                for (RqliteNode node : this.peers) {
                    try {
                        if (nodeRequestFactoryMap.containsKey(node)) {
                            requestFactory = nodeRequestFactoryMap.get(node);
                        } else {
                            requestFactory = new RequestFactory(node.proto, node.host, node.port);
                            nodeRequestFactoryMap.put(node, requestFactory);
                        }
                        GenericRequest r = requestFactory.AdoptRequest(request);
                        GenericResults results = r.execute();
                        return results;
                    } catch (IOException e) {
                    }
                }
                // pause to avoid churning
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
        throw new NodeUnavailableException("Could not connect to rqlite node.  Please check that the node is online and that your config files point to the correct address.");
    }

    private GenericResults tryOtherPeers(GenericRequest request, ParameterizedStatement[] stmts) throws NodeUnavailableException {
        // Cycle through the list of nodes in the config file.
        long end = System.currentTimeMillis() + timeoutDelay;
        if (peers != null) {
            while (System.currentTimeMillis() < end) {
                for (RqliteNode node : this.peers) {
                    try {
                        if (nodeRequestFactoryMap.containsKey(node)) {
                            requestFactory = nodeRequestFactoryMap.get(node);
                        } else {
                            requestFactory = new RequestFactory(node.proto, node.host, node.port);
                            nodeRequestFactoryMap.put(node, requestFactory);
                        }
                        GenericRequest r = requestFactory.AdoptRequest(request);
                        GenericResults results = r.execute();
                        return results;
                    } catch (IOException e) {
                    }
                }
                // pause to avoid churning
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
        throw new NodeUnavailableException("Could not connect to rqlite node.  Please check that the node is online and that your config files point to the correct address.");
    }

    public QueryResults Query(String[] stmts, boolean tx, ReadConsistencyLevel lvl) throws NodeUnavailableException {
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
        } catch (HttpResponseException responseException) {
            return (QueryResults) this.tryOtherPeers(request, stmts);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return (QueryResults) this.tryOtherPeers(request, stmts);
        }
    }
    @Override
    public QueryResults Query(ParameterizedStatement[] stmts, boolean tx, ReadConsistencyLevel lvl) throws NodeUnavailableException {
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
        } catch (HttpResponseException responseException) {
            return (QueryResults) this.tryOtherPeers(request, stmts);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return (QueryResults) this.tryOtherPeers(request, stmts);
        }
    }

    public QueryResults Query(String s, ReadConsistencyLevel lvl) throws NodeUnavailableException {
        return this.Query(new String[] { s }, false, lvl);
    }

    @Override
    public QueryResults Query(ParameterizedStatement q, ReadConsistencyLevel lvl) throws NodeUnavailableException {
        return this.Query(new ParameterizedStatement[] { q }, false, lvl);
    }

    public ExecuteResults Execute(String[] stmts, boolean tx) throws NodeUnavailableException {
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
        } catch (HttpResponseException responseException) {
            return (ExecuteResults) this.tryOtherPeers(request, stmts);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return (ExecuteResults) this.tryOtherPeers(request, stmts);
        }
    }

    @Override
    public ExecuteResults Execute(ParameterizedStatement[] stmts, boolean tx) throws NodeUnavailableException {
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
        } catch (HttpResponseException responseException) {
            return (ExecuteResults) this.tryOtherPeers(request, stmts);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return (ExecuteResults) this.tryOtherPeers(request, stmts);
        }
    }

    public ExecuteResults Execute(String s) throws NodeUnavailableException {
        return this.Execute(new String[] { s }, false);
    }

    @Override
    public ExecuteResults Execute(ParameterizedStatement q) throws NodeUnavailableException {
        return this.Execute(new ParameterizedStatement[]{ q }, false);
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
