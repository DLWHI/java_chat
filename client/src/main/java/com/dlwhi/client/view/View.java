package com.dlwhi.client.view;

public interface View {
    void show();

    void subscribe(String event, Call handler);
    void notifyRecieve(String message);

    void setActiveContext(String contextName);
    public void addContext(Menu context);
    public void addContext(String name, Menu context);
}
