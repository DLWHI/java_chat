package com.dlwhi.client.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Menu;

@Configuration
public class MenuBuilder {
    @Bean
    public Menu login() {
        Menu loginMenu = new Menu(System.out, new Scanner(System.in));
        loginMenu.addLine("Sign in");
        loginMenu.addLine("Sign up");
        loginMenu.addLine("Exit");
        return loginMenu;
    }

    @Bean
    public Menu main() {
        Menu mainMenu = new Menu(System.out, new Scanner(System.in));
        mainMenu.addLine("Enter room");
        mainMenu.addLine("Create room");
        mainMenu.addLine("Logout");
        mainMenu.addLine("Exit");
        return mainMenu;
    }
}
