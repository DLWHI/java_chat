package com.dlwhi.client.config;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.app.App.Command;
import com.dlwhi.client.menu.Menu;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.menu")
public class ClientConfig {
    @Autowired
    @Qualifier("loginMenu")
    private Menu login;

    @Autowired
    @Qualifier("mainMenu")
    private Menu main;

    @Bean
    public App configure() {
        App app = new App();
        configureBindings();
        app.addContext("login", login);
        app.setActiveContext("login");
        return app;
    }

    private void configureBindings() {
        try (Scanner parser = new Scanner(
                getClass()
                        .getResourceAsStream("/config/com/dlwhi/bindings.cfg"))) {
            while (parser.hasNextLine()) {
                try {
                    String[] bind = parser.nextLine().split("\\.|=");
                    switch (bind[0]) {
                        case "login":
                            login.addCommand(bind[2], Command.valueOf(bind[1].toUpperCase()));
                        case "main":
                            main.addCommand(bind[2], Command.valueOf(bind[1].toUpperCase()));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {

                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
