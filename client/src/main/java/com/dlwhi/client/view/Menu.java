package com.dlwhi.client.view;

import java.io.Console;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.dlwhi.client.chat.Command;

public class Menu implements Context {
    private static final String LINE = "---------------------%n";

    private final Console io;

    private HashMap<Integer, String> lines = new HashMap<>();
    private HashMap<Integer, Command> commands = new HashMap<>();

    public Menu(Console console) {
        io = console;
    }

    @Override
    public void show() {
        for (Map.Entry<Integer, String> line : lines.entrySet()) {
            io.printf("[%d] %s%n", line.getKey(), line.getValue());
        }
        io.printf(LINE);
    }

    @Override
    public Command requestCommand() {
        io.printf("-> ");
        String input = io.readLine();
        io.printf(LINE);
        Command command = null;
        try {
            command = commands.get(Integer.valueOf(input));
            if (command == null) {
                io.printf("Unknown command %s%n", input);
                io.printf(LINE);
            }
        } catch (NumberFormatException e) {
            io.printf("Unknown command %s%n", input);
            io.printf(LINE);
        }
        return command;
    }

    @Override
    public String requestInput(String message) {
        System.out.println(message);
        io.printf("-> ");
        String input = io.readLine();
        io.printf(LINE);
        return input;
    }

    @Override
    public StringBuilder requestSecretInput(String message) {
        System.out.println(message);
        io.printf("-> ");
        char[] input = io.readPassword();
        StringBuilder secret = new StringBuilder().append(input);
        io.printf(LINE);
        Arrays.fill(input, '[');
        return secret;
    }

    @Override
    public void notifyRecieve(String sender, String message) {
        io.printf("%s: %s%n", sender, message);
    }

    @Override
    public void notifyRecieveList(Map<Integer, String> list) {
        for (Map.Entry<Integer, String> line : list.entrySet()) {
            io.printf("[%d] %s%n", line.getKey(), line.getValue());
        }
        io.printf(LINE);
    }

    public void addLine(int index, String text) {
        lines.put(index, text);
    }

    public void addCommand(int bind, Command command) {
        commands.put(bind, command);
    }
}
