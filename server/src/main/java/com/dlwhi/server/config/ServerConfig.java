package com.dlwhi.server.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import com.dlwhi.server.controller.ServerController;
import com.dlwhi.server.repositories.TemplateUserRepository;
import com.dlwhi.server.repositories.UserRepository;
import com.dlwhi.server.services.ChatService;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:config/com/dlwhi/db.cfg")
@Import(ServerController.class)
public class ServerConfig {
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPasswd;
    @Value("${db.driver.name}")
    private String dbDriver;

    @Autowired
    @Qualifier("server")
    private ServerController server;

    @Bean
    public ServerController serverInstance() {
        return server;
    }

    @Bean
    public DataSource dataSourceHikari() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPasswd);
        dataSource.setDriverClassName(dbDriver);
        return dataSource;
    }

    @Bean
    public ChatService chatService() {
        ChatService service = new ChatService();
        service.setUserRepo(userRepository(dataSourceHikari()));
        return service;
    }

    @Bean
    @Scope(value = "prototype")
    public UserRepository userRepository(DataSource ds) {
        return new TemplateUserRepository(ds);
    }
}
