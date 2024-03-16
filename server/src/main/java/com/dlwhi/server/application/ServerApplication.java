package com.dlwhi.server.application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.dlwhi.server.client.Client;
import com.dlwhi.server.client.ClientObserver;
import com.dlwhi.server.client.ClientProvider;

@Component("server")
@PropertySource("classpath:config/com/dlwhi/server.cfg")
public class ServerApplication implements ClientObserver {
    private final ClientProvider provider;

    @Value("${server.port}")
    private int port;

    private boolean exited = false;

    private Set<Client> clients = new HashSet<>();

    @Autowired
    public ServerApplication(@Qualifier("provider") ClientProvider provider) {
        this.provider = provider;
    }

    public int exec() {
        Thread shutdownListener = new Thread(() -> {
            System.out.println("Closing all clients...");
            for (Client client : clients) {
                client.detachObserver();
                client.terminate();
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownListener);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.print("Server is up and listening ");
            System.out.println(server.getLocalSocketAddress());
            while (!exited) {
                Socket socket = server.accept();
                Client cl = provider.getClient(socket);
                cl.attachObserver(this);
                clients.add(cl);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0; 
    }

    @Override
    public void notifyDisconnect(Client who) {
        clients.remove(who);
    }

    @Override
    public void notifySend(String message, String author, long room) {
        for (Client client : clients) {
            try {
                if (client.isInRoom(room)) {
                    client.receiveMessage(message, author);
                }
            } catch (IOException e) {
                System.err.println("Unexpected IO exception");
                System.err.println(e.getMessage());
                client.terminate();
            }
        }
    }
}
