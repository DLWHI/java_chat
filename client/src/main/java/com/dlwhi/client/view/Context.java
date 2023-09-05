package com.dlwhi.client.view;

public interface Context {
    void show();
    String dispatchInput();

    void subscribe(String event, Call handler);
    void notifyRecieve(String message);
}
