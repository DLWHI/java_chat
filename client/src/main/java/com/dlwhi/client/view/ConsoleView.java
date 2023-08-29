package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.dlwhi.client.app.Controller;

@Component
public class ConsoleView implements View {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);
    
    private List<Controller> subscribers = new LinkedList<>();

    private Map<String, Menu> contexts = new HashMap<>();
    private Menu currentContext;

    private boolean exited = false;

    Thread viewThread;

    @Override
    public void show() {
        currentContext.display(out);
        out.println("---------------------");
        out.println("-> ");
        String cmd = currentContext.dispatchInput(in);
        for (Controller sub : subscribers) {
            sub.notifyController(cmd);
        }
    }

    @Override
    public void subscribeOnAllCommands(Controller subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void notifyRecieve(String message) {
        out.println(message);
    }

    @Override
    public void setActiveContext(String contextName) {
        currentContext = contexts.get(contextName);
    }

    public void addContext(Menu context) {
        contexts.put(context.getClass().getSimpleName(), context);
    }

    public void addContext(String name, Menu context) {
        contexts.put(name, context);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Unsupported operation 'close'");
    }
}
