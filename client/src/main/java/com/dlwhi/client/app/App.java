package com.dlwhi.client.app;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.dlwhi.client.model.Command;
import com.dlwhi.client.model.Connection;
import com.dlwhi.client.view.Call;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

public class App {
    @Value("${hostname:localhost}")
    private String hostname;
    @Value("${port:9857}")
    private int port;

    private Map<String, Call> events = new HashMap<>();

    private Map<String, Context> contexts = new HashMap<>();
    private Context currentContext;
    
    private boolean exited = false;

    public int exec() throws IOException {
        currentContext = contexts.get("login");
        try (Connection conn = new Connection(hostname, port)) {
            currentContext.notifyRecieve(conn.probe());
            while(!exited) {
                currentContext.show();
                currentContext.dispatchInput();
            }
        }
        return 0;
    }

    @Command("exit")
    private void exit() {
        exited = true;
    }

    @Command("sign_in")
    public void signIn(String user, String passwd) {
        System.out.println(user);
        System.out.println(passwd);
        // try {
        //     JSONCommand toSend = new JSONCommand("sign_in");
        //     toSend.addArgument(new JSONArgument("username", user)).addArgument(new JSONArgument("password", passwd));
        //     conn.send(toSend.toString());
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
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
}
