package com.dlwhi.server.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.server.config.ServerApplicationConfig;

public class MainServer {
    // TODO list
    // add package standart
    // add malformed package handling
    // refactor client interfaces
    public static void main( String[] args ) {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ServerApplicationConfig.class)) {
            ctx.registerShutdownHook();
            ServerApplication server = ctx.getBean("server", ServerApplication.class);
            server.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
