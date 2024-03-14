package com.dlwhi.server.client;

public interface ClientObserver {
    void notifyDisconnect(Client who);
}
