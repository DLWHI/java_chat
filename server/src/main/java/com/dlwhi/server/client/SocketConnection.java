package com.dlwhi.server.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.dlwhi.JSONObject;
public class SocketConnection implements Connection {
    private final Socket connection;
    private final BufferedReader in;
    private final BufferedWriter out;

    public SocketConnection(Socket connection) throws IOException {
        this.connection = connection;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        char[] probeData = new char[2];
        in.read(probeData);
        JSONObject res = new JSONObject().add("author", "server");
        if (probeData[0] == 0x0A && probeData[1] == 0x0D) {
            res.add("status", 200).add("message", "Hello from server");
            write(res.toString());
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
            try {
                JSONObject message = new JSONObject()
                    .add("status", 500)
                    .add("author", "server")
                    .add("message", "Server is closing connection");
                write(message.toString());
            } catch (IOException e) {
            }
            out.close();
            in.close();
            connection.close();
            System.out.println("Closed client " + connection);
        }
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
    public void send(JSONObject data) throws IOException {
        write(data.toString());
    }

    @Override
    public void sendNoThrow(JSONObject data) {
        try {
            write(data.toString());
        } catch (IOException e) {
        }
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
