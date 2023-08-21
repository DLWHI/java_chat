package com.dlwhi.server.app;

public class App {
    private boolean exited = false;

    public void configure() {

    }

    public int exec() {
        while (!exited) {

        }
        return 0; 
    }

    private void exit() {
        exited = true;
    }
}
