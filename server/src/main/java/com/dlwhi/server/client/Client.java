package com.dlwhi.server.client;

import java.io.IOException;

import com.dlwhi.JSONObject;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.UserService;

public class Client extends Thread {

    private final Connection conn;
    private final UserService users;

    private ClientObserver clientManager;

    private User userData;

    public Client(Connection conn, UserService users) {
        this.conn = conn;
        this.users = users;
    }

    @Override
    public void run() {
        try {
            while (!conn.isClosed()) {
                if (conn.ready()) {
                    JSONObject source = conn.accept();
                    if (source.getAsString("cmd").equals("sign_in")) {
                        login(source);
                    } else if (source.getAsString("cmd").equals("sign_up")) {
                        register(source);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO exception");
            System.err.println(e.getMessage() + " on " + conn);
        }
        terminate();
        if (clientManager != null) {
            clientManager.notifyDisconnect(this);
        }
    }

    public void terminate() {
        if (!conn.isClosed()) {
            try {
                conn.message("Server is closing connection");
                conn.close();
            } catch (IOException e) {
                System.err.println("Unexpected IO exception occured on closing " + conn);
                System.err.println(e.getMessage());
            }
        }
    }

    public void attachObserver(ClientObserver observer) {
        clientManager = observer;
    }

    public void detachObserver() {
        clientManager = null;
    }

    private void register(JSONObject data) throws IOException {
        if (users.register(data.getAsString("username"), data.getAsString("password"))) {
            conn.respondMessage(200, "Registered new user");
        } else {
            conn.respondMessage(401, "User exists");
        }
    }

    private void login(JSONObject data) throws IOException {
        userData = users.login(data.getAsString("username"), data.getAsString("password"));
        if (userData == null) {
            conn.respondMessage(401, "Invalid username or password");
        } else {
            conn.respondMessage(200, "Successful login");
        }
    }
}
