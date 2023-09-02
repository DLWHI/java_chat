package com.dlwhi.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.view.Option;

@Configuration
public class OptionsBuilder {
    @Bean
    public Option exit() { 
        return new Option("Exit");
    }

    @Bean
    public Option signIn() { 
        return new Option("Sign in");
    }

    @Bean
    public Option signUp() { 
        return new Option("Sign up");
    }
}
