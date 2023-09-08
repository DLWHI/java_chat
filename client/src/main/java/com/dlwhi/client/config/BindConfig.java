package com.dlwhi.client.config;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.exceptions.BadBindException;
import com.dlwhi.client.exceptions.NoSuchEventException;
import com.dlwhi.Command;
import com.dlwhi.Call;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

@Configuration
public class BindConfig {
    private static final String cfgFile = "/config/com/dlwhi/bindings.cfg";

    @Autowired
    private Map<String, Menu> menus;

    public void inject(App app) {
        bindCalls(menus, app);
        bindMenus();
        for (Map.Entry<String, Menu> menu : menus.entrySet()) {
            app.addContext(menu.getKey(), menu.getValue());
        }
        app.setActiveContext("login");
    }

    private void bindMenus() {
        try (Scanner reader = new Scanner(getClass().getResourceAsStream(cfgFile))) {
            reader.useDelimiter("[.=\r\n]");
            while (reader.hasNext()) {
                Menu menu = menus.get(reader.next());
                if (menu != null) {
                    try {
                        menu.addCommand(reader.next(), reader.next());
                    } catch(BadBindException e) {
                        System.err.println("Error: " + e.getMessage());
                        System.exit(-1);
                    } catch (NoSuchEventException e) {
                    }
                }
            }
        }
    }

    private void bindCalls(Map<String, Menu> menus, App app) {
        for (Method method : app.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Call call = new Call(app, method);
                Command cmd = method.getAnnotation(Command.class);
                String[] bindedContexts = cmd.context();
                if (bindedContexts.length > 0) {
                    bindToContexts(bindedContexts, cmd.value(), call);
                } else {
                    bindToAll(cmd.value(), call);
                }
            }
        }
    }

    private void bindToContexts(String[] contexts, String event, Call handler) {
        for (String context : contexts) {
            Context ctx = menus.get(context);
            if (ctx != null) {
                ctx.subscribe(event, handler);
            }
        }
    }

    private void bindToAll(String event, Call handler) {
        for (Context ctx : menus.values()) {
            ctx.subscribe(event, handler);
        }
    }
}
