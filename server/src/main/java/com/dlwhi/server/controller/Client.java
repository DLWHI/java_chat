package com.dlwhi.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.dlwhi.JSONObject;
import com.dlwhi.server.models.User;
import com.dlwhi.server.services.ChatService;

public class Client extends Thread implements Closeable {
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

    private void response(int status, String message) throws IOException {
        JSONObject res = new JSONObject().add("status", status).add("message", message);
        write(res.toString());
    }

    @Override
    public void run() {
        try {
            while (!connection.isClosed()) {
                String source = in.readLine();
                if (source != null) {
                    JSONObject data = JSONObject.fromString(source);
                    if (data.getAsString("command").equals("sign_in")) {
                        login(data);
                    } else if (data.getAsString("command").equals("sign_up")) {
                        register(data);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + " on " + connection.getInetAddress());
        }
        close();
    }

    private void login(JSONObject data) throws IOException {
        user = service.login(data.getAsString("username"),data.getAsString("password"));
        if (user == null) {
            response(401, "Invalid username or password");
        } else {
            response(200, "Successful login");
        }
    }

    private void register(JSONObject data) throws IOException {
        if (service.register(data.getAsString("username"),data.getAsString("password"))) {
            response(401, "Registered new user");
        } else {
            response(200, "User exists");
        }
    }

    @Override
    public void close() {
        if (!connection.isClosed()) {
            System.out.println("Closing client " + connection.getInetAddress() + "...");
            try {
                out.close();
                in.close();
                connection.shutdownInput();
                connection.shutdownOutput();
                connection.close();
            } catch (IOException e) {
                System.err.println("Unexpected IOException");
                System.err.println(e.getMessage());
            }
            System.err.println("Closed client " + connection.getInetAddress());
        }
    }
}
