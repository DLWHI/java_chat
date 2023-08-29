package com.dlwhi.client.view;

import java.io.Closeable;

import com.dlwhi.client.app.Controller;

public interface View extends Closeable {
    void show();
    void close();

    void notifyRecieve(String message);
    void subscribeOnAllCommands(Controller subscriber);

    void setActiveContext(String contextName);
    public void addContext(Menu context);
    public void addContext(String name, Menu context);
}
