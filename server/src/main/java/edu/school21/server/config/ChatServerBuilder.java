package edu.school21.server.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import com.zaxxer.hikari.HikariDataSource;

import edu.school21.server.repositories.IUserRepository;
import edu.school21.server.repositories.UserRepository;
import edu.school21.server.services.ChatService;

@Configuration
@PropertySource("classpath:config/db.properties")
public class ChatServerBuilder {
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
    public ChatService chatService() {
        ChatService service = new ChatService();
        service.setUserRepo(userRepository(dataSourceHikari()));
        return service;
    }

    @Bean
    @Scope(value = "prototype")
    public IUserRepository userRepository(DataSource ds) {
        return new UserRepository(ds);
    }
}
