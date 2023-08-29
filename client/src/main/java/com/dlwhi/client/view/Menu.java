package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.exceptions.InvalidCommandException;

public class Menu {
    private final String text;

    private Map<String, String> commands = new HashMap<>();

    public Menu(String text) {
        this.text = text;
    }

    public Menu(String text, Map<String, String> commands) {
        this.text = text;
        this.commands = commands;
    }

    public void display(PrintStream out) {
        out.println(text);
    }

    public String dispatchInput(Scanner in) {
        String input = in.nextLine();
        String command = commands.get(input);
        if (command == null) {
            throw new InvalidCommandException("Unknown command " + input);
        }
        return command;
    }

    public void addCommand(String value, String name) {
        commands.put(value, name);
    }

    public void setActiveCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
