package com.rqlite;

import com.rqlite.impl.RqliteImpl;

public enum RqliteFactory {
    INSTANCE; // XXX read about this! Enum-based factories.

    /**
     * Create a connection to a rqlite node.
     *
     * @param url
     *            the URL to connect to.
     * @return a rqlite client instance.
     */
    public static Rqlite connect(final String host, final Integer port) {
        return new RqliteImpl(host, port);
    }
}