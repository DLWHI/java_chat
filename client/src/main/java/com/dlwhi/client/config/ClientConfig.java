package com.dlwhi.client.config;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.dlwhi.client.view.Menu;
import com.dlwhi.client.view.View;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.view")
public class ClientConfig {
    private final String cmdConfigFile = "/config/com/dlwhi/commands.cfg";
    private final String menusConfigFolder = "/config/com/dlwhi/menus";

    private Map<String, Menu> menus;

    @Bean
    public View console() {
        View view = new ConsoleView();
        createMenus();
        configureBindings();
        view.addContext("login", menus.get("login"));
        view.setActiveContext("login");
        return view;
    }
    
    @Bean
    public App controller() {
        return new App(console());
    }

    private void configureBindings() {
        try (Scanner parser = new Scanner(
                getClass().getResourceAsStream(cmdConfigFile))) {
            while (parser.hasNextLine()) {
                try {
                    String[] bind = parser.nextLine().split("\\.|=");
                    menus.get(bind[0]).addCommand(bind[2], bind[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }
    
    private void createMenus() {
        menus = new HashMap<>();
        try {
            URL menuCfgs = getClass().getResource(menusConfigFolder);
            File dir = new File(menuCfgs.toURI());
            for (File cfg : dir.listFiles()) {
                try (Scanner reader = new Scanner(cfg)) {
                    reader.useDelimiter("\\Z");
                    menus.put(cfg.getName(), new Menu(reader.next()));
                } catch (FileNotFoundException e) {
                }
            }
        } catch (URISyntaxException e) {
        }
    }
}
