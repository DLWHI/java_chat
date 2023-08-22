package com.dlwhi.client.app;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.dlwhi.client.config.ClientConfig;

public class MainClient 
{
    public static void main( String[] args )
    {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext()) {
            ctx.registerShutdownHook();
            SimpleCommandLinePropertySource cmdOpt
                    = new SimpleCommandLinePropertySource(args);
            ctx.getEnvironment().getPropertySources().addFirst(cmdOpt);
            ctx.scan("com.dlwhi.client.config");
            ctx.refresh();
            App app = ctx.getBean("configure", App.class);
            app.exec();
        } catch (IOException e) {
            System.out.println("Unexpected IOException occured:");
            System.out.println(e.getMessage());
        }
    }
}
