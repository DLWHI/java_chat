package com.dlwhi.client.config;

import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.view.ConsoleView;
import com.dlwhi.client.view.Menu;
import com.dlwhi.client.view.View;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.view")
public class ClientConfig {
    private final String configFile = "/config/com/dlwhi/commands.cfg";

    @Autowired
    private Map<String, Menu> menus;

    
    @Bean
    public View console() {
        View view = new ConsoleView();
        configureBindings();
        view.addContext("login", menus.get("login"));
        view.setActiveContext("login");
        return view;
    }
    
    @Bean
    public App controller() {
        return new App(console());
    }

    // TODO remove switch case mechanism in favor of maps
    private void configureBindings() {
        try (Scanner parser = new Scanner(
                getClass().getResourceAsStream(configFile))) {
            while (parser.hasNextLine()) {
                try {
                    String[] bind = parser.nextLine().split("\\.|=");
                    menus.get(bind[0]).addCommand(bind[2], bind[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }
}
