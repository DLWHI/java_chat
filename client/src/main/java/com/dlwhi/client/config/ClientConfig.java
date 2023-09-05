package com.dlwhi.client.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.omg.CORBA.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dlwhi.client.app.App;

@Configuration
@ComponentScan(basePackages = "com.dlwhi.client.view")
public class ClientConfig {
    @Autowired
    private BindConfig binder;
    
    @Bean
    public App controller() {
        App app = new App();
        binder.inject(app);
        return app;
    }
}
