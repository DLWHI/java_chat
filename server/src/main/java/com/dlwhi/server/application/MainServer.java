package com.dlwhi.server.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.server.config.ServerConfig;

public class MainServer 
{
    public static void main( String[] args )
    {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ServerConfig.class)) {
            ctx.registerShutdownHook();
            ServerApplication server = ctx.getBean("server", ServerApplication.class);
            server.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
