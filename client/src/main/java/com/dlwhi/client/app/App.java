package com.dlwhi.client.app;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.factory.annotation.Value;

import com.dlwhi.client.model.Binded;
import com.dlwhi.client.model.Connection;
import com.dlwhi.client.model.User;
import com.dlwhi.client.view.Call;
import com.dlwhi.client.view.View;

public class App implements Controller {
    @Value("${hostname:localhost}")
    private String hostname;
    @Value("${port:9857}")
    private int port;

    private final View view;
    private User model;
    private boolean exited = false;

    public App(View view) {
        this.view = view;
        view.subscribe("exit", Call.extract(view, "exit"));
    }

    public int exec() throws IOException {
        try (Connection conn = new Connection(hostname, port)) {
            view.notifyRecieve(conn.probe());
            model = new User(conn);
            while(!exited) {
                view.show();
            }
        }
        return 0;
    }

    @Binded(command = "exit")
    private void exit() {
        exited = true;
    }

    public boolean notifyLogin(String[] args) {
        return true;
    }

    @Override
    public void notifySendMessage(String message) {
        
    }
}
