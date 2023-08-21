package com.dlwhi.server.exceptions;

public class MismatchedColumnsException extends RuntimeException {
    public MismatchedColumnsException(String message) {
        super(message);
    }
}
