package com.dlwhi.client.app;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;

import com.dlwhi.client.model.Binded;
import com.dlwhi.client.model.Connection;
import com.dlwhi.client.view.View;

// TODO add invocation type checks
public class App implements Controller {
    @Value("${hostname:localhost}")
    private String hostname;
    @Value("${port:9857}")
    private int port;

    private final View view;
    private boolean exited = false;

    public App(View view) {
        this.view = view;
        view.subscribeOnAllCommands(this);
    }

    public int exec() throws IOException {
        try (Connection conn = new Connection(hostname, port)) {
            view.notifyRecieve(conn.probe());
            while(!exited) {
                view.show();
            }
        }
        return 0;
    }

    @Binded(command = "exit")
    private void exit() {
        exited = true;
    }

    @Override
    public void notifyController(String command) {
        
    }
}
