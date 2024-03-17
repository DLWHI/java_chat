package com.dlwhi.client.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dlwhi.JSONObject;
import com.dlwhi.Command;
import com.dlwhi.client.model.Connection;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

@Component("container")
public class ClientApplication {
    @Value("${server.hostname}")
    private String hostname;
    @Value("${server.port:9857}")
    private int port;

    private Connection conn;

    private final Map<String, Context> contexts;
    private Context currentContext;
    
    private boolean exited = false;

    @Autowired
    ClientApplication(
        @Qualifier("contexts") Map<String, Context> contexts
    ) {
        this.contexts = contexts;
    }

    public int exec() throws IOException {
        currentContext = contexts.get("login");
        // conn = new Connection(hostname, port);
        // currentContext.notifyRecieve(conn.probe());
        while(!exited) {
            currentContext.show();
            currentContext.requestCommand();
        }
        return 0;
    }

    @Command("exit")
    private void exit() {
        exited = true;
    }

    @Command(
        value = "sign_in",
        context = {"login"}
    )
    public void signIn(String username, String password) {
        try {
            JSONObject toSend = 
            new JSONObject().add("cmd", "sign_in")
                .add("username", username)
                .add("password", password);
            conn.send(toSend.toString());
            JSONObject res = conn.waitForResponse();
            currentContext.notifyRecieve(res.getAsString("message"));
            if (res.getAsInt("status") == 200) {
                setActiveContext("main");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Command(
        value = "sign_up",
        context = {"login"}
    )
    public void signUp(String username, String password) {
        try {
            JSONObject toSend = 
            new JSONObject().add("cmd", "sign_up")
                .add("username", username)
                .add("password", password);
            conn.send(toSend.toString());
            JSONObject res = conn.waitForResponse();
            currentContext.notifyRecieve(res.getAsString("message"));       
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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
}
