package com.dlwhi.client.app;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.menu.Menu;

// TODO add invocation type checks
public class App {

    private String hostname;
    private int port;

    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    private boolean exited = false;

    private Map<String, Menu> contexts = new HashMap<>();
    private Menu currentContext;

    public App(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public int exec() throws IOException {
        try (Connection conn = new Connection(hostname, port)) {
            out.println(conn.probe());
            while (!exited) {
                currentContext.display(out);
                out.println("---------------------");
                out.print("-> ");
                call(currentContext.dispatchInput(in), conn);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Binded(command = "exit")
    private void exit() {
        exited = true;
    }

    public void setActiveContext(String contextName) {
        currentContext = contexts.get(contextName);
    }

    public void addContext(Menu context) {
        contexts.put(context.getClass().getSimpleName(), context);
    }

    public void addContext(String name, Menu context) {
        contexts.put(name, context);
    }

    private void call(String cmd, Connection target)
            throws IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Binded.class)) {
                Binded bind = method.getAnnotation(Binded.class);
                // add compile time parameter checks
                if (bind.command() == cmd) {
                    method.invoke(target, collectParameters(
                            bind.parameterNames()));
                }
            }
        }
    }

    private Object[] collectParameters(String[] paramNames) {
        Object[] params = new Object[paramNames.length];
        for (int i = 0; i < params.length; i++) {
            System.out.println("Enter " + paramNames[i]);
            params[i] = in.nextLine();
        }
        return params;
    }
}
