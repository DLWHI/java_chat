package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.exceptions.InvalidCommandException;

public class Menu {
    private Map<String, Option> content = new LinkedHashMap<>();

    public void display(PrintStream out) {
        int no = 0;
        for (Option option : content.values()) {
            System.out.printf("%d. ", ++no);
            option.display(out);
        }
    }

    public void dispatchInput(Scanner in) {
        String input = in.nextLine();
        Option target = content.get(input);
        if (content == null) {
            throw new InvalidCommandException("Unknown command " + input);
        } else {
            target.fire(in);
        }
        
    }

    public void addOption(Option value, String command) {
        content.put(command, value);
    }
}
