package com.dlwhi.server.client;

import java.io.IOException;

import com.dlwhi.JSONObject;
import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.RoomService;
import com.dlwhi.server.services.UserService;

public class Client extends Thread {

    private final Connection conn;
    private final UserService users;
    private final RoomService rooms;

    private ClientObserver clientManager;

    private User userData;
    private Room currentRoom;

    public Client(Connection conn, UserService users, RoomService rooms) {
        this.conn = conn;
        this.rooms = rooms;
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
                    } else if (source.getAsString("cmd").equals("search_room")) {
                        searchRooms(source);
                    } else if (source.getAsString("cmd").equals("enter")) {
                        enterRoom(source);
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
        Room[] found = rooms.find(data.getAsString("room_name")).toArray(new Room[0]);
        conn.respond(200, new JSONObject().add("rooms", found));
    }

    private void enterRoom(JSONObject data) throws IOException {
        currentRoom = rooms.find(data.getAsInt("room_id"));
        if (currentRoom == null) {
            conn.respond(200);
        } else {
            conn.respond(404);
        }
    }
}