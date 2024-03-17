package com.dlwhi.client.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dlwhi.client.config.ClientApplicationConfig;

public class MainClient 
{
    public static void main( String[] args ) {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ClientApplicationConfig.class)) {
            ctx.registerShutdownHook();
            ClientApplication app = ctx.getBean("container", ClientApplication.class);
            app.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
