package com.dlwhi.client.menu;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dlwhi.client.exceptions.InvalidCommandException;

@Component("login")
public class LoginMenu implements Menu {
    private static final String text =
        "1. Sign In%n" +
        "2. Sign Up%n" +
        "3. Exit%n";

    private Map<String, String> commands = new HashMap<>();

    @Override
    public void display(PrintStream out) {
        out.printf(text);
    }

    @Override
    public String dispatchInput(Scanner in) {
        String input = in.nextLine();
        String command = commands.get(input);
        if (command == null) {
            throw new InvalidCommandException("Unknown command " + input);
        }
        return command;
    }

    @Override
    public void addCommand(String value, String name) {
        commands.put(value, name);
    }

    @Override
    public void setActiveCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
