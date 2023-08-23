package com.dlwhi.client.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dlwhi.client.json.JSONArgument;
import com.dlwhi.client.json.JSONCommand;

public class Connection implements Closeable {
    private final char[] probeCharset = new char[]{0x0A, 0x0D};

    private final Socket socket;

    private final BufferedReader in;
    private final BufferedWriter out;

    private int userID;
    private String user;

    public Connection(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public String probe() throws IOException {
        out.write(probeCharset);
        out.flush();
        return in.readLine();
    }

    public void send(String packet) throws IOException {
        out.write(packet + "\n");
        out.flush();
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
