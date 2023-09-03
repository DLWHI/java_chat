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
        built.addOption(options.get("signIn"), "sign_in");
        built.addOption(options.get("signUp"), "sign_up");
        built.addOption(options.get("exit"), "exit");
        
        return built;
    }

    @Bean
    public Menu main() {
        Menu built = new Menu();
        return built;
    }
}
