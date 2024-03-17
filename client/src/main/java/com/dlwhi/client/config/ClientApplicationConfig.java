package com.dlwhi.client.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

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
        try {
            contexts.put("login", login());
            contexts.put("main", main());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return contexts;
    }

    @Bean
    @Qualifier("loginMenu")
    public Menu login() throws IOException {
        Menu loginMenu = new Menu(System.console());
        try (InputStreamReader cfgReader =
                new InputStreamReader(getClass().getResourceAsStream(CFG_FILEPATH));
        ) {
            MenuCfgParser cfgParser = new MenuCfgParser(cfgReader);
            cfgParser.parseContext("login", loginMenu);
        }
        return loginMenu;
    }

    @Bean
    @Qualifier("mainMenu")
    public Menu main() throws IOException {
        Menu mainMenu = new Menu(System.console());
        try (InputStreamReader cfgReader =
                new InputStreamReader(getClass().getResourceAsStream(CFG_FILEPATH));
        ) {
            MenuCfgParser cfgParser = new MenuCfgParser(cfgReader);
            cfgParser.parseContext("main", mainMenu);
        }
        return mainMenu;
    }
}
