package com.dlwhi.server.client;

import java.io.IOException;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dlwhi.server.services.UserService;

@Component("provider")
public class ClientProvider {
    private final UserService userService;

    @Autowired
    public ClientProvider(@Qualifier("chatService") UserService userService) {
        this.userService = userService;
    }

    public Client getClient(Socket connection) {
        try {
            Client cl = new Client(new ClientConnection(connection), userService);
            cl.start();
            return cl;
        } catch (IOException e) {
            // TODO: handle exception
        }
        return null;
    }
}
