package com.dlwhi.client.chat;

import java.util.Map;

public interface ConnectionObserver {
    void notifyRecieve(String sender, String message);
    void notifyRecieveList(Map<Integer, String> list);
}
