package com.rqlite;

/**
 * This exception is thrown when rqlite-java cannot connect to a rqlite node.
 **/
public class NodeUnavailableException extends Exception {
    public NodeUnavailableException(String message){
        super(message);
    }
}