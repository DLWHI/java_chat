package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.dlwhi.client.exceptions.InvalidCommandException;

@Component
public class ConsoleView implements View {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    private Map<String, Menu> contexts = new HashMap<>();
    private Menu currentContext;

    @Override
    public void show() {
        currentContext.display(out);
        out.println("---------------------");
        out.println("-> ");
        currentContext.dispatchInput(in);
    }

    @Override
    public void notifyRecieve(String message) {
        out.println(message);
    }

    @Override
    public void setActiveContext(String contextName) {
        currentContext = contexts.get(contextName);
    }

    @Override
    public void addContext(Menu context) {
        contexts.put(context.getClass().getSimpleName(), context);
    }

    @Override
    public void addContext(String name, Menu context) {
        contexts.put(name, context);
    }
}
