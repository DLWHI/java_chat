package edu.school21.client.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.school21.client.config.ClientConfig;

public class Main 
{
    public static void main( String[] args )
    {
        try (AnnotationConfigApplicationContext ctx = 
                new AnnotationConfigApplicationContext(ClientConfig.class)) {
            ctx.registerShutdownHook();
            App app = ctx.getBean("configure", App.class);
            app.exec();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
