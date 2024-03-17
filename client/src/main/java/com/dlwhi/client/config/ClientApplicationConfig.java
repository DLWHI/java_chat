package com.dlwhi.client.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.dlwhi.client.application.ClientApplication;
import com.dlwhi.client.view.Context;
import com.dlwhi.client.view.Menu;

@Configuration
@PropertySource("classpath:config/com/dlwhi/settings.cfg")
@Import({ClientApplication.class})
public class ClientApplicationConfig {
    private static final String CFG_FILEPATH = "/config/com/dlwhi/contexts.cfg";
    
    @Bean
    @Qualifier("contexts")
    public HashMap<String, Context> contexts() {
        HashMap<String, Context> contexts = new HashMap<>();
        contexts.put("login", login());
        contexts.put("main", main());
        return contexts;
    }

    @Bean
    @Qualifier("loginMenu")
    public Menu login() {
        Menu loginMenu = new Menu(System.out, new Scanner(System.in));
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(CFG_FILEPATH))) {
            MenuCfgParser parser = new MenuCfgParser(reader);
            parser.parseContext("login", loginMenu);
        } catch (IOException e) {
            // TODO: handle exception
        }
        return loginMenu;
    }

    @Bean
    @Qualifier("mainMenu")
    public Menu main() {
        Menu mainMenu = new Menu(System.out, new Scanner(System.in));
        return mainMenu;
    }
}
