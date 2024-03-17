package com.dlwhi.client.view;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class Menu implements Context {
    private static final String LINE = "---------------------%n";

    private final Console io;

    private HashMap<Integer, String> lines = new HashMap<>();
    private HashMap<Integer, String> commands = new HashMap<>();

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
    public String requestCommand() throws InvalidCommandException {
        io.printf("-> ");
        String input = io.readLine();
        String command = commands.get(Integer.valueOf(input));
        if (command == null) {
            throw new InvalidCommandException("Unkown command " + input);
        }
        io.printf(LINE);
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
    public void notifyRecieve(String message) {
        io.printf(message + "%n");
    }

    public void addLine(int index, String text) {
        lines.put(index, text);
    }

    public void addCommand(int bind, String command) {
        commands.put(bind, command);
    }
}
