package com.dlwhi.client.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    public void call(String command, String[] args) 
        throws IllegalAccessException,
            IllegalArgumentException, 
            InvocationTargetException {
        commands.get(command).invoke(command, (Object[])args);
    }

    public void bindMethods() {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Binded.class)) {
                Binded bind = method.getAnnotation(Binded.class);
                method.setAccessible(true);
                commands.put(bind.command(), method);
            }
        }
    }

    // TODO map methods to commands
    @Binded(command = "sign_in")
    public void signIn(String user, String passwd) {
        try {
            JSONCommand toSend = new JSONCommand("sign_in");
            toSend.addArgument(new JSONArgument("username", user)).addArgument(new JSONArgument("password", passwd));
            conn.send(toSend.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Binded(command = "sign_up")
    public void signUp() {

    }
}
