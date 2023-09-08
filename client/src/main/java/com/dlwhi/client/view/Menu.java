package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.exceptions.BadBindException;
import com.dlwhi.client.exceptions.InvalidCommandException;
import com.dlwhi.client.exceptions.NoSuchEventException;

public class Menu implements Context {
    private final PrintStream out;
    private final Scanner in;

    private Integer lineCount = 0;
    private String content = "";
    private Map<String, String> commands = new HashMap<>();
    private Map<String, Call> calls = new HashMap<>();

    public Menu(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void show() {
        out.printf(content);
        out.println("---------------------");
        out.println("-> ");
    }

    @Override
    public String dispatchInput() throws InvalidCommandException {
        String input = in.nextLine();
        String command = commands.get(input.toLowerCase());
        if (command == null) {
            throw new InvalidCommandException("Unkown command " + input);
        }
        calls.get(command).invoke(collectParameters(calls.get(command)));
        out.println("---------------------");
        return command;
    }

    @Override
    public void subscribe(String event, Call handler) {
        calls.put(event, handler);
    }

    @Override
    public void notifyRecieve(String message) {
        out.println(message);
    }

    public void addLine(String text) {
        content += (++lineCount).toString() + ". " + text + "%n";
    }

    public void addCommand(String command, String alias) throws NoSuchEventException {
        if (!calls.containsKey(command)) {
            throw new NoSuchEventException("Unknown command for context");
        } else if (commands.containsKey(alias)) {
            throw new BadBindException("Duplicate bind to alias \"" + alias + "\"");
        }
        commands.put(alias.toLowerCase(), command);
    }

    private Object[] collectParameters(Call call) {
        Object[] params = call.getParameterArray();
        for (int i = 0; i < params.length; i++) {
            System.out.println("Enter " + call.getParameterName(i));
            params[i] = in.nextLine();
        }
        return params;
    }
}
