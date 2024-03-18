package com.dlwhi.client.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dlwhi.JSONObject;

public class SocketConnection implements Closeable {
    private final Socket socket;

    private final BufferedReader in;
    private final BufferedWriter out;

    public SocketConnection(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendRaw(char[] bytes) throws IOException {
        out.write(bytes);
        out.flush();
    }

    public void send(JSONObject data) throws IOException {
        out.write(data.toString() + "\n");
        out.flush();
    }

    public JSONObject read() throws IOException {
        String data = in.readLine();
        if (data == null) {
            return null;
        }
        return JSONObject.fromString(data);
    }

    @Override
    public void close() {
        if (!socket.isClosed()) {
            System.out.println("Closing connection...");
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Unexpected IOException");
                System.err.println(e.getMessage());
            }
            System.err.println("Connection closed");
        }
    }
}
