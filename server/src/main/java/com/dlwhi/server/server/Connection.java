package com.dlwhi.server.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection extends Thread implements Closeable {
    private final Socket connection;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Connection(Socket connection) throws IOException {
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

    @Override
    public void run() {
        try {
            // TODO add disconnect option
            while (true) {
                String data = in.readLine();
                System.out.println(data);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + " on " + connection.getInetAddress());
        }
        close();
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
