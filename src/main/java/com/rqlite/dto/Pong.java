package com.rqlite.dto;

public class Pong {
    public String version;

    public Pong() {
        this.version = "unknown";
    }

    public Pong(final String version) {
        this.version = version;
    }
}
