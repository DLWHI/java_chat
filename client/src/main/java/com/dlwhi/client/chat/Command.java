package com.dlwhi.client.chat;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    SIGN_IN("signIn"),
    SIGN_UP("signUp"),
    LOG_OUT("logOut"),
    ENTER_ROOM("enterRoom"),
    FIND_ROOM("findRoom"),
    EXIT("exit");

    private static Map<String, Command> values;
    static {
        values = new HashMap<>();
        for (Command value : values()) {
            values.put(value.cmd, value);
        }
    }

    private final String cmd;

    Command(String cmd) {
        this.cmd = cmd;
    }

    public static Command fromString(String value) {
        return values.get(value);
    }

    @Override
    public String toString() {
        return cmd;
    }
}
