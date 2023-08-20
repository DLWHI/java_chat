package edu.school21.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import edu.school21.client.app.App;
import edu.school21.client.menu.Menu;

@Configuration
@ComponentScan(basePackages = "edu.school21.client.menu")
public class ClientConfig {
    @Autowired
    @Qualifier("startMenu")
    private Menu start;

    @Bean
    public App configure() {
        App app = new App();
        app.addContext("startMenu", start);
        app.setActiveContext("startMenu");
        return app;
    }
}
