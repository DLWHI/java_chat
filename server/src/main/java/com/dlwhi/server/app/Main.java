package com.dlwhi.server.app;

public class Main 
{
    public static void main( String[] args )
    {
        App app = new App();
        app.configure();
        System.exit(app.exec());
    }
}
