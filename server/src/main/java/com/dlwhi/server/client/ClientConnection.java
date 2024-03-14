package com.dlwhi.server.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.dlwhi.JSONObject;
public class ClientConnection implements Connection {
    private final Socket connection;
    private final BufferedReader in;
    private final BufferedWriter out;

    public ClientConnection(Socket connection) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        char[] probeData = new char[2];
        in.read(probeData);
        if (probeData[0] == 0x0A && probeData[1] == 0x0D) {
            message("Hello from server");
        } else {
            throw new UnknownClientException(connection);
        }
        System.out.println("Connected client " + connection);
    }

    private void write(String data) throws IOException {
        out.write(data + "\n");
        out.flush();
    }

    @Override
    public void close() throws IOException {
        if (!connection.isClosed()) {
            System.out.println("Closing client " + connection + "...");
            out.close();
            in.close();
            connection.close();
            System.out.println("Closed client " + connection);
        }
    }

    @Override
    public boolean ready() throws IOException {
        return in.ready();
    }

    @Override
    public JSONObject accept() throws IOException {
        String source = in.readLine();
        if (source != null) {
            return JSONObject.fromString(source);
        }
        return null;
    }

    @Override
    public void respond(int status, JSONObject data) throws IOException {
        write(data.add("status", status).toString());
    }

    @Override
    public void message(String message) throws IOException {
        write(new JSONObject().add("message", message).toString());
    }

    @Override
    public void respondMessage(int status, String message) throws IOException {
        respond(status, new JSONObject().add("message", message));
    }

    @Override
    public boolean isClosed() {
        return connection.isClosed();
    }

    @Override
    public String toString() {
        return "Client" + connection;
    }
}
