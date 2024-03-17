package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu implements Context {
    private final PrintStream out;
    private final Scanner in;

    private HashMap<Integer, String> lines = new HashMap<>();
    private HashMap<String, String> commands = new HashMap<>();

    public Menu(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void show() {
        for (Map.Entry<Integer, String> line : lines.entrySet()) {
            out.println("[" + line.getKey() + "] " + line.getValue());
        }
        out.println("---------------------");
    }

    @Override
    public String requestCommand() throws InvalidCommandException {
        out.printf("-> ");
        String input = in.nextLine();
        String command = commands.get(input.toLowerCase());
        if (command == null) {
            throw new InvalidCommandException("Unkown command " + input);
        }
        out.println("---------------------");
        return command;
    }

    @Override
    public String requestInput(String message) {
        System.out.println(message);
        out.printf("-> ");
        String input = in.nextLine();
        out.println("---------------------");
        return input;
    }


    @Override
    public void notifyRecieve(String message) {
        out.println(message);
    }

    public void addLine(int index, String text) {
        if (text == null || index < 0) {
            // throw ?
        }
        lines.put(index, text);
    }

    public void addCommand(String command, String alias) {
        commands.put(alias.toLowerCase(), command);
    }
}
