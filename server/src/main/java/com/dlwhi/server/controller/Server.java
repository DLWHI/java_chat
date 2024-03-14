package com.dlwhi.server.controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.dlwhi.server.services.ChatService;

@Component("server")
@PropertySource("classpath:config/com/dlwhi/server.cfg")
public class Server {
    private final ChatService service;

    @Value("${server.port}")
    private int port;

    private boolean exited = false;

    private List<Client> clients = new ArrayList<>();

    @Autowired
    public Server(@Qualifier("chatService") ChatService service) {
        this.service = service;
    }

    public int exec() {
        Thread shutdownListener = new Thread(() -> {
            System.out.println("Closing all clients...");
            for (Client client : clients) {
                client.close();
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownListener);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.print("Server is up and listening ");
            System.out.println(server.getLocalSocketAddress());
            while (!exited) {
                Socket socket = server.accept();
                Client connected = new Client(socket, service);
                clients.add(connected);
                connected.start();
                System.out.println("Active connections: " + clients.size());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0; 
    }
}
