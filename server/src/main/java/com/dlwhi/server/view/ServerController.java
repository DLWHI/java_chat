package com.dlwhi.server.view;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.dlwhi.server.services.ChatService;

@Component("server")
@PropertySource("classpath:config/com/dlwhi/server.cfg")
public class ServerController {
    @Autowired
    private final ChatService service = null;

    @Value("${server.port}")
    private int port;

    private boolean exited = false;

    private List<Client> clients = new LinkedList<>();


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

    private void exit() {
        exited = true;
    }
}
