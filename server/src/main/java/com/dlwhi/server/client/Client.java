package com.dlwhi.server.client;

import java.io.IOException;

import com.dlwhi.JSONObject;
import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.MessageService;
import com.dlwhi.server.services.RoomService;
import com.dlwhi.server.services.UserService;

public class Client extends Thread {

    private final Connection conn;
    private final UserService users;
    private final RoomService rooms;
    private final MessageService msgs;

    private ClientObserver clientManager;

    private User userData;
    private Room currentRoom;

    public Client(Connection conn, UserService users, RoomService rooms, MessageService msgs) {
        this.conn = conn;
        this.rooms = rooms;
        this.users = users;
        this.msgs = msgs;
    }

    @Override
    public void run() {
        try {
            JSONObject source;
            while ((source = conn.accept()) != null) {
                if (source.getAsString("cmd").equals("sign_in")) {
                    login(source);
                } else if (source.getAsString("cmd").equals("sign_up")) {
                    register(source);
                } else if (source.getAsString("cmd").equals("search_room")) {
                    searchRooms(source);
                } else if (source.getAsString("cmd").equals("enter")) {
                    enterRoom(source);
                } else if (source.getAsString("cmd").equals("send")) {
                    sendMessage(source);
                } else {
                    System.err.println("Unknown command " + source.getAsString("cmd"));
                }
            }
            conn.close();
        } catch (IOException e) {
            System.err.println("Unexpected IO exception");
            System.err.println(e.getMessage() + " on " + conn);
        }
        if (clientManager != null) {
            clientManager.notifyDisconnect(this);
        }
    }

    public void terminate() {
        if (!conn.isClosed()) {
            try {
                conn.message("Server is closing connection");
            } catch (IOException e) {
                System.err.println("Unexpected IO exception occured on closing " + conn);
                System.err.println(e.getMessage());
            }
        }
    }

    public void receiveMessage(String text, String author) throws IOException {
        if (!conn.isClosed()) {
            conn.respond(200, new JSONObject().add("text", text).add("author", author));
        }
    }

    public boolean isInRoom(long roomId) {
        if (currentRoom != null) {
            return currentRoom.getId() == roomId;
        }
        return false;
    }

    public void attachObserver(ClientObserver observer) {
        clientManager = observer;
    }

    public void detachObserver() {
        clientManager = null;
    }

    private void register(JSONObject data) throws IOException {
        if (users.register(data.getAsString("username"), data.getAsString("password"))) {
            conn.respond(200);
        } else {
            conn.respond(401);
        }
    }

    private void login(JSONObject data) throws IOException {
        userData = users.login(data.getAsString("username"), data.getAsString("password"));
        if (userData == null) {
            conn.respond(401);
        } else {
            conn.respond(200);
        }
    }

    private void searchRooms(JSONObject data) throws IOException {
        Room[] found = rooms.findRoom(data.getAsString("room_name")).toArray(new Room[0]);
        conn.respond(200, new JSONObject().add("rooms", found));
    }

    private void enterRoom(JSONObject data) throws IOException {
        currentRoom = rooms.findRoom(data.getAsInt("room_id"));
        if (currentRoom == null) {
            conn.respond(200);
        } else {
            conn.respond(404);
        }
    }

    private void sendMessage(JSONObject source) throws IOException {
        String text = source.getAsString("text");
        msgs.createMessage(text, currentRoom.getId(), userData);
        if (clientManager != null) {
            clientManager.notifySend(text, userData.getUsername(), currentRoom.getId());
        }
        conn.respond(200);
    }
}
