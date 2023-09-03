package com.dlwhi.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Menu;

@Configuration
public class MenuBuilder {
    
    @Bean
    public Menu login() {
        Menu built = new Menu();
        built.addLine("Sign in");
        built.addLine("Sign up");
        built.addLine("Exit");

        return built;
    }

    @Bean
    public Menu main() {
        Menu built = new Menu();
        return built;
    }
}
