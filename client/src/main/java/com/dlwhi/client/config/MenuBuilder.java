package com.dlwhi.client.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Menu;
import com.dlwhi.client.view.Option;

@Configuration
public class MenuBuilder {
    @Autowired
    private Map<String, Option> options;

    @Bean
    public Menu login() {
        Menu built = new Menu();
        built.addOption(options.get("signIn"), "1");
        built.addOption(options.get("signIn"), "Sign In");

        built.addOption(options.get("signUp"), "2");
        built.addOption(options.get("signUp"), "Sign Up");

        built.addOption(options.get("exit"), "3");
        built.addOption(options.get("exit"), "Exit");
        return built;
    }

    @Bean
    public Menu main() {
        Menu built = new Menu();
        return built;
    }
}
