package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.exceptions.BadBindException;
import com.dlwhi.client.exceptions.InvalidCommandException;

public class Menu {
    private Integer lineCount = 0;
    private String content = "";
    private Map<String, String> binds = new HashMap<>();

    public void display(PrintStream out) {
        System.out.printf(content);
    }

    public String dispatchInput(Scanner in) {
        String input = in.nextLine();
        String command = binds.get(input);
        if (command == null) {
            throw new InvalidCommandException("Unkown command " + input);
        }
        return command;
    }

    public void addLine(String text) {
        content += (++lineCount).toString() + ". " + text + "%n";
    }

    public void addCommand(String command, String alias) {
        binds.put(alias, command);
    }
}
