package com.dlwhi.client.config;

import java.util.Map;
import java.util.Scanner;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Menu;


@Configuration
public class BindConfig {
    private static final String cfgFile = "/config/com/dlwhi/bindings.cfg";

    public void bindMenus(Map<String, Menu> menus) {
        try (Scanner reader = new Scanner(getClass().getResourceAsStream(cfgFile))) {
            reader.useDelimiter("[.=\n]");
            while (reader.hasNext()) {
                Menu menu = menus.get(reader.next());
                if (menu != null) {
                    menu.addCommand(reader.next(), reader.next());
                }
            }
        }
    }

    
}
