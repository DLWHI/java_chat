package com.dlwhi.server.client;

import java.io.IOException;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dlwhi.server.services.RoomService;
import com.dlwhi.server.services.UserService;

@Component("provider")
public class ClientProvider {
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public ClientProvider(
        @Qualifier("chatService") UserService userService,
        @Qualifier("chatService") RoomService roomService
    ) {
        this.userService = userService;
        this.roomService = roomService;
    }

    public Client getClient(Socket connection) {
        try {
            Client cl = new Client(new ClientConnection(connection), userService, roomService);
            cl.start();
            return cl;
        } catch (IOException e) {
            System.err.println("Unexpected IO exception on " + connection);
            System.err.println(e.getMessage());
        }
        return null;
    }
}
