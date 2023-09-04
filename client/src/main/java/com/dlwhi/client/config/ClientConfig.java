package com.dlwhi.client.config;

import java.util.Map;

import javax.annotation.PostConstruct;

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
    @Autowired
    private Map<String, Menu> menus;

    @Autowired
    private BindConfig binder;

    @PostConstruct
    private void bind() {
        binder.bindMenus(menus);
    }

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
