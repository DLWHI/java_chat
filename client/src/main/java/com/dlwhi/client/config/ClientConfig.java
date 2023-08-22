package com.dlwhi.client.config;

import java.util.Map;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.app.App.Command;
import com.dlwhi.client.menu.Menu;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.menu")
public class ClientConfig {
    private final String configFile = "/config/com/dlwhi/commands.cfg";

    @Autowired
    private Map<String, Menu> menus;

    @Value("${hostname:localhost}")
    private String hostname;
    @Value("${port:9857}")
    private int port;

    @Bean
    public App configure() {
        App app = new App(hostname, port);
        configureBindings();
        app.addContext("login", menus.get("login"));
        app.setActiveContext("login");
        return app;
    }

    // TODO remove switch case mechanism in favor of maps
    private void configureBindings() {
        try (Scanner parser = new Scanner(
                getClass().getResourceAsStream(configFile))) {
            while (parser.hasNextLine()) {
                try {
                    String[] bind = parser.nextLine().split("\\.|=");
                    menus.get(bind[0]).addCommand(bind[2], Command.valueOf(bind[1].toUpperCase()));
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }
}
