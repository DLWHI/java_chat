package edu.school21.server.app;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    public enum Bindings {
        EXIT("Exit");

        private String[] commands;

        private Bindings(String command) {
            commands = new String[]{command};
        }

        private Bindings(String[] commands) {
            this.commands = commands;
        }

        public boolean equals(String command) {
            if (Arrays.stream(commands).anyMatch(command::equals)) {
                return true;
            }
            return false;
        }
    }

    private Scanner in = new Scanner(System.in);
    private PrintStream out = System.out;
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
