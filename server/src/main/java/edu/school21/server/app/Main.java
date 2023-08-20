package edu.school21.server.app;

import com.beust.jcommander.JCommander;

public class Main 
{
    public static void main( String[] args )
    {
        App app = new App();
        JCommander.newBuilder().addObject(app).build().parse(args);
        app.configure();
        System.exit(app.exec());
    }
}
