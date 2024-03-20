package com.dlwhi.client.application;

import java.io.IOException;
import java.util.HashMap;
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

    public int exec() {
        try {
            currentContext = contexts.get("login");
            conn = new SocketConnection(hostname, port);
            probe();
            while (!exited) {
                currentContext.show();
                commandRouter(currentContext.requestCommand());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private void commandRouter(Command command) throws IOException {
        if (command == null) {
            return;
        }
        JSONObject data = new JSONObject().add("cmd", command.toString());
        switch (command) {
            case SIGN_IN:
                signIn(data);
                break;
            case ENTER_ROOM:
                enterRoom(data);
                break;
            case EXIT:
                exited = true;
                break;
            case LOG_OUT:
                logout(data);
                break;
            case SIGN_UP:
                signUp(data);
                break;
        }
    }

    private void probe() throws IOException {
        conn.sendRaw(PROBE_CHARSET);
        JSONObject res = conn.read();
        currentContext.notifyRecieve(
            res.getAsString("author"),
            res.getAsString("message")
        );
    }

    private void signIn(JSONObject data) throws IOException {
        String login = currentContext.requestInput("Login:");
        StringBuilder passwd = currentContext.requestSecretInput("Password:");
        conn.send(data.add("login", login).add("password", passwd));
        for (int i = 0; i < passwd.length(); ++i) {
            passwd.setCharAt(i, ']');
        }
        data = conn.read();
        currentContext.notifyRecieve(
            data.getAsString("author"),
            data.getAsString("message")
        );
        if (data.getAsInt("status") == 200) {
            setActiveContext("main");
        }
    }

    private void signUp(JSONObject data) throws IOException {
        String login = currentContext.requestInput("Login:");
        StringBuilder passwd = currentContext.requestSecretInput("Password:");
        conn.send(data.add("login", login).add("password", passwd));
        for (int i = 0; i < passwd.length(); ++i) {
            passwd.setCharAt(i, ']');
        }
        data = conn.read();
        currentContext.notifyRecieve(
            data.getAsString("author"),
            data.getAsString("message")
        );
    }

    private void logout(JSONObject data) throws IOException {
        conn.send(data);
        data = conn.read();
        currentContext.notifyRecieve(
            data.getAsString("author"),
            data.getAsString("message")
        );
        setActiveContext("login");
    }

    private void enterRoom(JSONObject data) throws IOException {
        String roomName = currentContext.requestInput("Enter room name:");
        data.add("room_name", roomName);
        conn.send(data);
        data = conn.read();
        currentContext.notifyRecieve(
            data.getAsString("author"),
            data.getAsString("message")
        );
        if (data.getAsInt("status") == 200) {
            int length = data.getAsInt("length");
            Map<Integer, String> rooms = new HashMap<>();
            for (int i = 0; i < length; ++i) {
                data = conn.read();
                rooms.put(data.getAsInt("roomId"), data.getAsString("roomName"));
            }
            currentContext.notifyRecieveList(rooms);
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
