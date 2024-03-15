package com.dlwhi.server.client;

import java.io.Closeable;
import java.io.IOException;

import com.dlwhi.JSONObject;

public interface Connection extends Closeable {
    boolean isClosed();

    boolean ready() throws IOException;
    JSONObject accept() throws IOException;
    void respond(int status) throws IOException;
    void respond(int status, JSONObject data) throws IOException;
    void message(String message) throws IOException;
}
