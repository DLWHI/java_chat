package com.dlwhi.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Menu;

@Configuration
public class MenuBuilder {
    @Bean
    public Menu login() {
        Menu loginMenu = new Menu();
        loginMenu.addLine("Sign in");
        loginMenu.addLine("Sign up");
        loginMenu.addLine("Exit");
        return loginMenu;
    }

    @Bean
    public Menu main() {
        Menu mainMenu = new Menu();
        mainMenu.addLine("Enter room");
        mainMenu.addLine("Create room");
        mainMenu.addLine("Logout");
        mainMenu.addLine("Exit");
        return mainMenu;
    }
}
