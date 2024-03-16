package com.dlwhi.server.client;

public interface ClientObserver {
    void notifyDisconnect(Client who);

    void notifySend(String message, String author, long room);
}
