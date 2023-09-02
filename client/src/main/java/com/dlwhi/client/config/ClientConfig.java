package com.dlwhi.client.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.sound.midi.Patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;
import com.dlwhi.client.view.ConsoleView;
import com.dlwhi.client.view.Option;
import com.dlwhi.client.view.Menu;
import com.dlwhi.client.view.View;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.view")
public class ClientConfig {
    private final String cmdConfigFile = "/config/com/dlwhi/commands.cfg";

    @Autowired
    private Map<String, Menu> menus;

    @Bean
    public View console() {
        View view = new ConsoleView();
        view.addContext("login", menus.get("login"));
        view.setActiveContext("login");
        return view;
    }
    
    @Bean
    public App controller() {
        return new App(console());
    }
}
