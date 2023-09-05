package com.dlwhi.client.config;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.exceptions.NoSuchEventException;
import com.dlwhi.client.model.Command;
import com.dlwhi.client.view.Call;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;


@Configuration
public class BindConfig {
    private static final String cfgFile = "/config/com/dlwhi/bindings.cfg";

    @Autowired
    private Map<String, Menu> menus;

    public void inject(App app) {
        bindCalls(menus, app);
        for (Map.Entry<String, Menu> menu : menus.entrySet()) {
            app.addContext(menu.getKey(), menu.getValue());
        }
        app.setActiveContext("login");
    }

    @PostConstruct
    private void bindMenus() {
        try (Scanner reader = new Scanner(getClass().getResourceAsStream(cfgFile))) {
            reader.useDelimiter("[.=\r\n]");
            while (reader.hasNext()) {
                Menu menu = menus.get(reader.next());
                if (menu != null) {
                    menu.addCommand(reader.next(), reader.next());
                }
            }
        }
    }

    private void bindCalls(Map<String, Menu> menus, App app) {
        for (Method method : app.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command cmd = method.getAnnotation(Command.class);
                for (Context ctx : menus.values()) {
                    try {
                        ctx.subscribe(cmd.value(), new Call(app, method));
                    } catch (NoSuchEventException e) {
                    }
                }
            }
        }
    }
}
