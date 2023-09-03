package com.dlwhi.client.view;

public interface View {
    void show();

    void notifyRecieve(String message);

    void setActiveContext(String contextName);
    public void addContext(Menu context);
    public void addContext(String name, Menu context);
}
