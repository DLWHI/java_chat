package com.dlwhi.server.client;

import java.io.IOException;

public interface Client {
    public void end();

    public void receiveMessage(String text, String author) throws IOException;

    boolean isInRoom(long roomId);

    void attachObserver(ClientObserver observer);
    void detachObserver(ClientObserver observer);
}
