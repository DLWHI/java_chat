package com.dlwhi.server.client;

import java.net.Socket;

public class UnknownClientException extends RuntimeException {
    public UnknownClientException(Socket connection) {
        super("Invalid probe byteset on " + connection.getInetAddress());
    }
}
