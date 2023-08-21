package com.dlwhi.client.app;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.dlwhi.client.menu.Menu;

public class App {
    public enum Command {
        SIGN_IN,
        SIGN_UP,
        EXIT;
    }

    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    private boolean exited = false;

    private Map<String, Menu> contexts = new TreeMap<>();
    private Menu currentContext;

    public int exec() {
        while (!exited) {
            try {
                currentContext.display(out);
                out.println("---------------------");
                out.print("-> ");
                call(currentContext.dispatchInput(in));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }

    @Binded(command = Command.EXIT)
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

    private void call(Command cmd)
            throws IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Binded.class)) {
                Binded bind = method.getAnnotation(Binded.class);
                // add compile time parameter checks
                if (bind.command() == cmd) {
                    method.invoke(this, collectParameters(
                                    bind.parameterNames(),
                                    method.getParameterTypes()));
                }
            }
        }
    }

    private Object[] collectParameters(String[] paramNames, Class<?>... paramTypes) {
        Object[] params = new Object[paramNames.length];
        for (int i = 0; i < params.length; i++) {
            System.out.println("Enter " + paramNames[i]);
            params[i] = in.nextLine();
        }
        return params;
    }

    @Binded(command = Command.SIGN_IN, parameterNames = { "username", "password" })
    private void signIn(String username, String passwd) {

    }

    @Binded(command = Command.SIGN_UP, parameterNames = { "username", "password" })
    private void signUp(String username, String passwd) {

    }
}
