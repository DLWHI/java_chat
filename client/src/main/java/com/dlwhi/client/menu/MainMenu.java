package com.dlwhi.client.menu;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import com.dlwhi.client.app.App.Command;
import com.dlwhi.client.exceptions.InvalidCommandException;

@Component("main")
public class MainMenu implements Menu {
    private static final String text =
        "1. Enter a room%n" +
        "2. Create room%n" +
        "3. Logout%n" +
        "4. Exit%n";

    private Map<String, Command> commands = new HashMap<>();

    @Override
    public void display(PrintStream out) {
        out.printf(text);
    }

    @Override
    public Command dispatchInput(Scanner in) {
        String input = in.nextLine();
        Command command = commands.get(input);
        if (command == null) {
            throw new InvalidCommandException("Unknown command " + input);
        }
        return command;
    }

    @Override
    public void addCommand(String value, Command name) {
        commands.put(value, name);
    }

    @Override
    public void setActiveCommands(Map<String, Command> commands) {
        this.commands = commands;
    }
}
