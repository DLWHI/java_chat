package com.dlwhi.client.model;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import com.dlwhi.client.json.JSONArgument;
import com.dlwhi.client.json.JSONCommand;

public class User {
    private final Connection conn;
    private Map<String, Method> commands;

    public User(Connection conn) {
        this.conn = conn;
    }

    // TODO map methods to commands
    @Binded(command = "sign_in", parameterNames = { "username", "password" })
    public void signIn(String username, String passwd) {
        try {
            JSONCommand toSend = new JSONCommand("sign_in");
            toSend.addArgument(new JSONArgument("username", username)).addArgument(new JSONArgument("password", passwd));
            conn.send(toSend.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Binded(command = "sign_up", parameterNames = { "username", "password" })
    public void signUp(String username, String passwd) {

    }
}
