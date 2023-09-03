package com.dlwhi.client.config;

import java.util.Scanner;

import org.springframework.context.annotation.Configuration;


@Configuration
public class BindConfig {
    private static final String cfgFile = ":/config/com/dlwhi/bindings.cfg";

    public BindConfig() {
        try (Scanner reader = new Scanner(getClass().getResourceAsStream(cfgFile))) {
            reader.useDelimiter(". =\n");
            
        }
    }
}
