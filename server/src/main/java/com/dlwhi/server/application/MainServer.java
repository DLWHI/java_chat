package com.dlwhi.server.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.server.config.ServerApplicationConfig;

public class MainServer {
    // TODO list
    // add malformed package handling
    // add finalizers to connections
    public static void main( String[] args ) {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ServerApplicationConfig.class)) {
            ctx.registerShutdownHook();
            ServerApplication server = ctx.getBean("server", ServerApplication.class);
            server.exec();
        }
    }
}
