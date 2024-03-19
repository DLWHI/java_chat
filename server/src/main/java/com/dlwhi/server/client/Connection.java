package com.dlwhi.server.client;

import java.io.Closeable;
import java.io.IOException;

import com.dlwhi.JSONObject;

public interface Connection extends Closeable {
    boolean isClosed();

    JSONObject accept() throws IOException;
    void send(JSONObject data) throws IOException;
    void sendNoThrow(JSONObject data);
}
