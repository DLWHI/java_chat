package com.dlwhi.client.chat;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    SIGN_IN("sign_in"),
    SIGN_UP("sign_up"),
    LOG_OUT("log_out"),
    ENTER_ROOM("enter_room"),
    FIND_ROOM("find_room"),
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
