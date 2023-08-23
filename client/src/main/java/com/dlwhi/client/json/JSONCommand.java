package com.dlwhi.client.json;

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

    @Override
    public String toString() {
        String json = "{command:" + cmd;
        for (JSONArgument jsonArgument : args) {
            json += "," + jsonArgument.toString();
        }
        return json + "}";
    }
}
