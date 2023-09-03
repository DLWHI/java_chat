package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.exceptions.BadBindException;
import com.dlwhi.client.exceptions.InvalidCommandException;

public class Menu {
    private Map<String, Option> content = new LinkedHashMap<>();
    private Map<String, String> binds = new HashMap<>();

    public void display(PrintStream out) {
        int no = 0;
        for (Option option : content.values()) {
            System.out.printf("%d. ", ++no);
            option.display(out);
        }
    }

    public void dispatchInput(Scanner in) {
        String input = in.nextLine();
        String alias = binds.get(input.toLowerCase());
        if (alias == null) {
            throw new InvalidCommandException("Unknown command " + input);
        } else {
            content.get(alias).fire(in);
        }
        
    }

    public void addOption(Option value, String name) {
        content.put(name, value);
    }

    public void bind(String optionName, String alias) {
        binds.put(alias, optionName);
    }

    public void subscribe(String option, Call action) throws BadBindException {
        Option opt = content.get(option);
        if (action == null) {
            throw new BadBindException("Empty call");
        }
        if (opt == null) {
            throw new BadBindException("Option " + option + " does not exist");
        }
        opt.onChoose(action);
    }
}
