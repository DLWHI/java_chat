package com.dlwhi.client.json;

import java.util.LinkedList;
import java.util.List;

import com.dlwhi.client.app.App.Command;

public class JSONCommand {
    private Command cmd;
    private List<JSONArgument> args = new LinkedList<>();
    
    public JSONCommand(Command cmd) {
        this.cmd = cmd;
    }

    public JSONCommand addArgument(JSONArgument arg) {
        args.add(arg);
        return this;
    }

    @Override
    public String toString() {
        String json = "{command:" + cmd.toString();
        for (JSONArgument jsonArgument : args) {
            json += "," + jsonArgument.toString();
        }
        return json + "}";
    }
}
