package com.dlwhi;

import java.util.HashMap;
import java.util.Map;

public class JSONObject implements Cloneable {
    private JSONObject parent = null;
    private Map<String, Object> args = new HashMap<>();

    public JSONObject() {
    }

    public JSONObject(JSONObject parent) {
        this.parent = parent;
    }

    public int size() {
        return args.size();
    }

    public static JSONObject fromString(String source) {
        return JSONParser.parseString(source);
    }

    public JSONObject add(String key, Object value) {
        args.put(key, value);
        return this;
    }

    public JSONObject add(String key, JSONObject object) {
        args.put(key, object);
        object.parent = this;
        return this;
    }

    public JSONObject remove(String key) {
        args.remove(key);
        return this;
    }

    public Object get(String param) {
        return args.get(param);
    }

    public String getAsString(String param) {
        return args.get(param).toString()   ;
    }

    // unsafe
    public int getAsInt(String param) {
        return (int)args.get(param);
    }

    public <T> T getAs(String param, Class<T> type) {
        Object target = args.get(param);
        if (target == null || !type.isInstance(target)) {
            return null;
        }
        return type.cast(target);
    }

    public boolean hasKey(String key) {
        return args.containsKey(key);
    }

    public JSONObject getParent() {
        return parent;
    }

    public JSONObject getChild(String key) {
        Object child = args.get(key);
        if (child instanceof JSONObject && child != null) {
            return (JSONObject) child;
        }
        return null;
    }

    public JSONObject clear() {
        args.clear();
        return this;
    }

    @Override
    public String toString() {
        String json = "{";
        String separator = "";
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            json += separator + JSONPrinter.printEntry(entry.getKey(), entry.getValue());
            separator = ",";
        }
        return json + "}";
    }
    
    @Override
    public Object clone() {
        JSONObject cl = new JSONObject(parent);
        cl.args.putAll(args);
        return cl;
    }
}
