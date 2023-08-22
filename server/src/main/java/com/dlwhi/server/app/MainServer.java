package com.dlwhi.server.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.server.config.ServerConfig;
import com.dlwhi.server.server.Server;

public class MainServer 
{
    public static void main( String[] args )
    {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ServerConfig.class)) {
            ctx.registerShutdownHook();
            Server server = ctx.getBean("serverInstance", Server.class);
            server.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
