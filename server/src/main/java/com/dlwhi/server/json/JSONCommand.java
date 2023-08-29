package com.dlwhi.server.json;

import java.util.LinkedList;
import java.util.List;

public class JSONCommand {
    private String cmd;
    private List<JSONArgument> args = new LinkedList<>();
    
    public JSONCommand(String cmd) {
        this.cmd = cmd;
    }

    public JSONCommand addArgument(JSONArgument arg) {
        args.add(arg);
        return this;
    }

    public static JSONCommand parse() {
        return null;
    }

    @Override
    public String toString() {
        String json = "{command:" + cmd;
        for (JSONArgument jsonArgument : args) {
            json += "," + jsonArgument.toString();
        }
        return json + "}";
    }
}
