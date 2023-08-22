package com.dlwhi.server.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("server")
@PropertySource("classpath:config/com/dlwhi/server.cfg")
public class Server {
    @Value("${server.port}")
    private int port;

    private boolean exited = false;

    private List<Connection> clients = new LinkedList<>();

    public int exec() {
        Thread shutdownListener = new Thread(() -> {
            System.out.println("Closing all clients...");
            for (Connection client : clients) {
                client.close();
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownListener);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.print("Server is up and listening ");
            System.out.println(server.getLocalSocketAddress());
            while (!exited) {
                Socket socket = server.accept();
                Connection connected = new Connection(socket);
                clients.add(connected);
                connected.start();
                System.out.println("Active connections: " + clients.size());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0; 
    }

    private void exit() {
        exited = true;
    }
}
