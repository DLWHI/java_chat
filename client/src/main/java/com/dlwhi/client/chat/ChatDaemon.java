package com.dlwhi.client.chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.dlwhi.JSONObject;

public class ChatDaemon extends Thread {
    private final Set<ConnectionObserver> observers = new HashSet<>();
    
    private final SocketConnection conn;

    public ChatDaemon(SocketConnection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        try {
            JSONObject res;
            while ((res = conn.read()) != null) {
                String sender = res.getAsString("author");
                String message = res.getAsString("message");
                for (ConnectionObserver observer : observers) {
                    observer.notifyRecieve(sender, message);
                }
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }
    
    public void attachObserver(ConnectionObserver obs) {
        observers.add(obs);
    }

    public void detachObserver(ConnectionObserver obs) {
        observers.remove(obs);
    }
}
