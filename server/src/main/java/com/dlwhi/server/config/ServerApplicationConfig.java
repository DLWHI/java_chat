package com.dlwhi.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.dlwhi.server.application.ServerApplication;
import com.dlwhi.server.client.ClientProvider;
import com.dlwhi.server.repositories.RoomRepository;
import com.dlwhi.server.repositories.TemplateRoomRepository;
import com.dlwhi.server.repositories.TemplateUserRepository;
import com.dlwhi.server.repositories.UserRepository;
import com.dlwhi.server.services.ChatService;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:config/com/dlwhi/db.cfg")
@Import({ClientProvider.class, ServerApplication.class})
public class ServerApplicationConfig {
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPasswd;
    @Value("${db.driver.name}")
    private String dbDriver;

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
    @Autowired
    public ChatService chatService(
        @Qualifier("templateUserRepository") UserRepository userRepo,
        @Qualifier("templateUserRepository") RoomRepository roomRepo
    ) {
        return new ChatService(userRepo, roomRepo);
    }

    @Bean
    @Autowired
    public TemplateUserRepository templateUserRepository(@Qualifier("dataSourceHikari") DataSource ds) {
        return new TemplateUserRepository(ds);
    }

    @Bean
    @Autowired
    public TemplateRoomRepository templateRoomRepository(@Qualifier("dataSourceHikari") DataSource ds) {
        return new TemplateRoomRepository(ds);
    }
}