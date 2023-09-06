package com.dlwhi.client.app;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.dlwhi.JSONPackage;
import com.dlwhi.client.model.Command;
import com.dlwhi.client.model.Connection;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

public class App implements Closeable {
    @Value("${hostname:localhost}")
    private String hostname;
    @Value("${port:9857}")
    private int port;

    private Connection conn;

    private Map<String, Context> contexts = new HashMap<>();
    private Context currentContext;
    
    private boolean exited = false;

    public int exec() throws IOException {
        currentContext = contexts.get("login");
        conn = new Connection(hostname, port);
        currentContext.notifyRecieve(conn.probe());
        while(!exited) {
            currentContext.show();
            currentContext.dispatchInput();
        }
        return 0;
    }

    @Command("exit")
    private void exit() {
        exited = true;
    }

    @Command("sign_in")
    public void signIn(String username, String password) {
        try {
            JSONPackage toSend = 
            new JSONPackage("command", "sign_in")
                .add("username", username)
                .add("password", password);
            conn.send(toSend.toString());
            System.out.println(conn.waitForResponse());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Command("sign_up")
    public void signUp() {

    }
    
    public void setActiveContext(String contextName) {
        currentContext = contexts.get(contextName);
    }

    public void addContext(Menu context) {
        contexts.put(context.getClass().getSimpleName(), context);
    }

    public void addContext(String name, Menu context) {
        contexts.put(name, context);
    }

    @Override
    public void close() throws IOException {
        conn.close();
    }
}
