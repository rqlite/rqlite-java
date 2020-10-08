package com.rqlite.dto;

public class Pong implements GenericResults {
    public String version;

    public Pong() {
        this.version = "unknown";
    }

    public Pong(final String version) {
        this.version = version;
    }
}
