package com.dlwhi.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import com.dlwhi.JSONPackage;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.ChatService;

public class Client extends Thread implements Closeable {
    private static Map<String, Call> events;
    
    private final Socket connection;
    private final BufferedReader in;
    private final BufferedWriter out;

    private final ChatService service;

    private User user;

    public Client(Socket connection, ChatService service) throws IOException {
        this.service = service;
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        char[] probeData = new char[2];
        in.read(probeData);
        if (probeData[0] == 0x0A && probeData[1] == 0x0D) {
            write("Hello from server");
        } else {
            // throw
        }
        System.out.println("Connected client " + connection.getInetAddress());
    }

    private void write(String data) throws IOException {
        out.write(data + "\n");
        out.flush();
    }

    private void response(String status, String message) throws IOException {
        JSONPackage res = new JSONPackage("status", status).add("message", message);
        write(res.toString());
    }

    @Override
    public void run() {
        try {
            while (true) {
                JSONPackage data = JSONPackage.fromString(in.readLine());
                // TODO client factory
                // Call event = events.get(data.getAsString("command"));
                // if (event != null) {
                //     event.invoke(data);
                // }
                if (data.getAsString("command").equals("sign_in")) {
                    login(data);
                } else if (data.getAsString("command").equals("sign_up")) {
                    register(data);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + " on " + connection.getInetAddress());
        }
        close();
    }

    @Command("sign_in")
    private void login(JSONPackage data) throws IOException {
        user = service.login(data.getAsString("username"),data.getAsString("password"));
        if (user == null) {
            response("fail", "Invalid username or password");
        } else {
            response("success", "Successful login");
        }
    }

    @Command("sign_up")
    private void register(JSONPackage data) throws IOException {
        if (service.register(data.getAsString("username"),data.getAsString("password"))) {
            response("success", "Registered new user");
        } else {
            response("fail", "User exists");
        }
    }

    @Override
    public void close() {
        if (!connection.isClosed()) {
            System.out.println("Closing client " + connection.getInetAddress() + "...");
            try {
                out.close();
                in.close();
                connection.close();
            } catch (IOException e) {
                System.err.println("Unexpected IOException");
                System.err.println(e.getMessage());
            }
            System.err.println("Closed client " + connection.getInetAddress());
        }
    }
}
