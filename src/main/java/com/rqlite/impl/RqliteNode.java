package com.rqlite.impl;

public class RqliteNode {
    public String proto;
    public String host;
    public Integer port;

    public RqliteNode(String proto, String host, Integer port){
        this.proto = proto;
        this.host = host;
        this.port = port;
    }
}
