package com.dlwhi.client.view;

import com.dlwhi.client.chat.Command;
import com.dlwhi.client.chat.ConnectionObserver;

public interface Context extends ConnectionObserver {
    void show();
    Command requestCommand();
    String requestInput(String message);
    StringBuilder requestSecretInput(String message);
}
