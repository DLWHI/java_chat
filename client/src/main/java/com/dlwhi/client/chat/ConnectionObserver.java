package com.dlwhi.client.chat;

public interface ConnectionObserver {
    void notifyRecieve(String sender, String message);
}
