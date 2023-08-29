package com.dlwhi.server.view;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.server.config.ServerConfig;

public class MainServer 
{
    public static void main( String[] args )
    {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ServerConfig.class)) {
            ctx.registerShutdownHook();
            ServerController server = ctx.getBean("serverInstance", ServerController.class);
            server.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
