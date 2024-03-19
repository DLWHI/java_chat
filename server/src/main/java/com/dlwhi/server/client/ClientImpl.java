package com.dlwhi.server.client;

import java.io.IOException;

import org.springframework.dao.DataAccessException;

import com.dlwhi.JSONObject;
import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.MessageService;
import com.dlwhi.server.services.RoomService;
import com.dlwhi.server.services.UserService;

public class ClientImpl extends Thread implements Client {

    private final Connection conn;
    private final UserService users;
    private final RoomService rooms;
    private final MessageService msgs;

    private ClientObserver clientManager;

    private User userData;
    private Room currentRoom;
    private boolean isEnded = false;

    public ClientImpl(Connection conn, UserService users, RoomService rooms, MessageService msgs) {
        this.conn = conn;
        this.rooms = rooms;
        this.users = users;
        this.msgs = msgs;
    }

    @Override
    public void run() {
        try {
            JSONObject source;
            // TODO: router and fix malformed json handling
            while ((source = conn.accept()) != null && !isEnded) {
                commandRouter(source);
            }
            conn.close();
        } catch (IOException e) {
            System.err.println(e.getMessage() + " on " + conn);
        }
        if (clientManager != null) {
            clientManager.notifyDisconnect(this);
        }
    }

    @Override
    public void disconnect() {
        if (!conn.isClosed()) {
            try {
                conn.close();
            } catch (IOException e) {
                System.err.println("Unexpected IO exception occured on closing " + conn);
                System.err.println(e.getMessage());
            } 
        }
        isEnded = true;
    }

    @Override
    public void receiveMessage(String text, String author) throws IOException {
        if (!conn.isClosed()) {
            JSONObject message = new JSONObject()
                .add("status", 200)
                .add("author", author)
                .add("message", text);
            conn.send(message);
        }
    }

    @Override
    public boolean isInRoom(long roomId) {
        if (currentRoom != null) {
            return currentRoom.getId() == roomId;
        }
        return false;
    }

    @Override
    public void attachObserver(ClientObserver observer) {
        clientManager = observer;
    }

    @Override
    public void detachObserver(ClientObserver observer) {
        clientManager = null;
    }

    private void commandRouter(JSONObject data) throws IOException {
        try {
            Command cmd = Command.fromString(data.getAsString("cmd"));
            switch (cmd) {
                case ENTER_ROOM:
                    break;
                case EXIT:
                    break;
                case FIND_ROOM:
                    break;
                case LOG_OUT:
                    logout();
                    break;
                case SIGN_IN:
                    login(data);
                    break;
                case SIGN_UP:
                    register(data);
                    break;
                default:
                    conn.send(new JSONObject()
                        .add("status", 500)
                        .add("author", "server")
                        .add("message", "Unknown command")
                    );
                    break;
            }
        } catch (DataAccessException e) {
            conn.send(new JSONObject()
                .add("status", 500)
                .add("author", "server")
                .add("message", "Internal server error")
            );
        } 
    }

    private void register(JSONObject data) throws IOException {
        JSONObject res = new JSONObject().add("author", "server");
        String name = data.getAsString("login");
        String passwd = data.getAsString("password");
        if (name == null || passwd == null || name.isEmpty() || passwd.isEmpty()) {
            res.add("status", 400).add("message", "Invalid data provided");
        } else if (users.register(name, passwd)) {
            res.add("status", 200).add("message", "Registration successful. Sign in to start chatting");
        } else {
            res.add("status", 400).add("message", "User exists");
        }
        conn.send(res);
    }

    private void login(JSONObject data) throws IOException {
        JSONObject res = new JSONObject().add("author", "server");
        String name = data.getAsString("login");
        String passwd = data.getAsString("password"); 
        if (name == null || passwd == null) {
            res.add("status", 400).add("message", "Invalid data provided");
        }
        userData = users.login(name, passwd);
        if (userData == null) {
            res.add("status", 403).add("message", "Invalid username or password");
        } else {
            res.add("status", 200).add("message", "Successful login");
        }
        conn.send(res);
    }

    private void logout() throws IOException {
        userData = null;
        JSONObject res = new JSONObject()
            .add("status", 200)
            .add("author", "server")
            .add("message", "Logout successful");
        conn.send(res);
    }

    // TODO: fix complex object serialization
    private void searchRooms(JSONObject data) throws IOException {
        Room[] found = rooms.findRoom(data.getAsString("room_name")).toArray(new Room[0]);
        JSONObject res = new JSONObject()
            .add("status", 200)
            .add("author", "server")
            .add("message", "Found rooms:")
            .add("rooms", found);
        conn.send(res);
    }

    private void enterRoom(JSONObject data) throws IOException {
        currentRoom = rooms.findRoom(data.getAsInt("room_id"));
        JSONObject res = new JSONObject().add("author", "server");
        if (currentRoom == null) {
            res.add("status", 404).add("message", "Room does not exist");
        } else {
            res.add("status", 200)
                .add("message", "Entered roomId " + currentRoom.getId() + ". Last messages:");
        }
        conn.send(res);
    }

    private void sendMessage(JSONObject source) throws IOException {
        String text = source.getAsString("text");
        msgs.createMessage(text, currentRoom.getId(), userData);
        if (clientManager != null) {
            clientManager.notifySend(text, userData.getUsername(), currentRoom.getId());
        }
    }
}
