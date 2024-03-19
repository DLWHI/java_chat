package com.dlwhi.client.view;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super("Unkown command " + message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super("Unkown command " + message, cause);
    }
}
