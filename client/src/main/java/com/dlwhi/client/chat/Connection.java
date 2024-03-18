package com.dlwhi.client.chat;

import java.io.Closeable;
import java.io.IOException;

import com.dlwhi.JSONObject;

public interface Connection extends Closeable {
    public void send(JSONObject data) throws IOException;
    public void sendRaw(char[] bytes) throws IOException;
    public JSONObject read() throws IOException;
}
