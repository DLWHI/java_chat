package com.dlwhi.client.view;

public interface Context {
    void show();
    String requestCommand() throws InvalidCommandException;
    String requestInput(String message);

    void notifyRecieve(String message);
}
