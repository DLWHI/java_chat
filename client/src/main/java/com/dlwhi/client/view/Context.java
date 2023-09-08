package com.dlwhi.client.view;

import com.dlwhi.Call;

public interface Context {
    void show();
    String dispatchInput();

    void subscribe(String event, Call handler);
    void notifyRecieve(String message);
}
