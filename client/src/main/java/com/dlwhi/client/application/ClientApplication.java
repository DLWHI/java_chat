package com.dlwhi.client.application;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dlwhi.JSONObject;
import com.dlwhi.client.chat.Command;
import com.dlwhi.client.chat.SocketConnection;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

@Component("container")
public class ClientApplication {
    private final char[] PROBE_CHARSET = new char[]{0x0A, 0x0D};
   
    @Value("${server.hostname}")
    private String hostname;
    @Value("${server.port:9857}")
    private int port;
  
    private SocketConnection conn;

    private final Map<String, Context> contexts;
    private Context currentContext;

    private boolean exited = false;

    @Autowired
    ClientApplication(
            @Qualifier("contexts") Map<String, Context> contexts) {
        this.contexts = contexts;
    }

    public int exec() throws IOException {
        currentContext = contexts.get("login");
        conn = new SocketConnection(hostname, port);
        probe();
        while (!exited) {
            currentContext.show();
            commandRouter(currentContext.requestCommand());
        }
        return 0;
    }

    private void commandRouter(Command command) throws IOException {
        JSONObject jsonData = new JSONObject().add("cmd", command.toString());
        switch (command) {
            case SIGN_IN:
                signIn(jsonData);
                break;
            case ENTER_ROOM:
                break;
            case EXIT:
                exited = true;
                break;
            case FIND_ROOM:
                break;
            case LOG_OUT:
                break;
            case SIGN_UP:
                signUp(jsonData);
                break;
        }
    }

    private void probe() throws IOException {
        conn.sendRaw(PROBE_CHARSET);
        JSONObject res = conn.read();
        currentContext.notifyRecieve(
            res.getAsString("author"),
            conn.read().getAsString("message")
        );
    }

    private void signIn(JSONObject data) throws IOException {
        String login = currentContext.requestInput("Login:");
        char[] passwd = currentContext.requestSecretInput("Password:");
        conn.send(data.add("login", login).add("passwd", passwd));
        Arrays.fill(passwd, ' ');
        JSONObject res = conn.read();
        currentContext.notifyRecieve(
            res.getAsString("author"),
            conn.read().getAsString("message")
        );
        if (res.getAsInt("status") == 200) {
            setActiveContext("main");
        }
    }

    private void signUp(JSONObject data) throws IOException {
        String login = currentContext.requestInput("Login:");
        char[] passwd = currentContext.requestSecretInput("Password:");
        conn.send(data.add("login", login).add("passwd", passwd));
        JSONObject res = conn.read();
        currentContext.notifyRecieve(
            res.getAsString("author"),
            conn.read().getAsString("message")
        );
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
