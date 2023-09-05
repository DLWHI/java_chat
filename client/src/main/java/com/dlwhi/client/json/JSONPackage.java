package com.dlwhi.client.json;

import java.util.LinkedHashMap;
import java.util.Map;


public class JSONPackage {
    private Map<String, Object> args = new LinkedHashMap<>();

    public JSONPackage() {
    }

    public JSONPackage(String param, Object value) {
        args.put(param, value);
    }

    public JSONPackage add(String param, Object value) {
        args.put(param, value);
        return this;
    }

    @Override
    public String toString() {
        String json = "{";
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            if (entry.getValue() instanceof Number) {
                json += entry.getKey() + ": " + entry.getValue().toString() + ",";
            } else {
                json += entry.getKey() + ": \"" + entry.getValue().toString() + "\",";
            }
        }
        return json + "}";
    }
}
