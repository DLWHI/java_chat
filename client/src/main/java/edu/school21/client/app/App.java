package edu.school21.client.app;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import edu.school21.client.menu.Menu;


public class App {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    private boolean exited = false;

    private Map<String, Menu> contexts = new TreeMap<>();
    private Menu currentContext;

    public int exec() {
        while (!exited) {
            currentContext.display(out);
            out.println("---------------------");
            out.print("-> ");
            System.out.println(currentContext.dispatchInput(in));
            ;
        }
        return 0; 
    }

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

    @Binded(command = "menu.sign_in")
    public void signIn() {

    }

    @Binded(command = "menu.sign_up")
    public void signUp() {
        
    }
}
