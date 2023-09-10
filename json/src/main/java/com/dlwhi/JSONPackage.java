package com.dlwhi;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JSONPackage {
    private JSONPackage parent = null;
    private Map<String, Object> args = new LinkedHashMap<>();

    public JSONPackage() {
    }

    public JSONPackage(JSONPackage parent) {
        this.parent = parent;
    }

    public JSONPackage(String param, Object value) {
        args.put(param, value);
    }

    public JSONPackage add(String param, Object value) {
        args.put(param, value);
        return this;
    }

    public Object get(String param) {
        return args.get(param);
    }

    public String getAsString(String param) {
        return args.get(param).toString()   ;
    }

    public <T> T getAs(String param, Class<T> type) {
        Object target = args.get(param);
        if (target == null && !type.isInstance(target)) {
            return null;
        }
        return type.cast(target);
    }

    public JSONPackage getParent() {
        return parent;
    }

    public JSONPackage clear() {
        args.clear();
        return this;
    }

    @Override
    public String toString() {
        String json = "{";
        Set<Map.Entry<String, Object>> entries = args.entrySet();
        Iterator<Map.Entry<String, Object>> i = entries.iterator();
        Map.Entry<String, Object> entry;
        if (i.hasNext()) {
            entry = i.next();
            json += "\"" + entry.getKey() + "\":";
            if (entry.getValue() instanceof Number) {
                json += entry.getValue().toString();
            } else {
                json += "\"" + entry.getValue().toString() + "\"";
            }
        }
        while (i.hasNext()) {
            entry = i.next();
            json += ",";
            json += "\"" + entry.getKey() + "\":";
            if (entry.getValue() instanceof Number) {
                json += entry.getValue().toString();
            } else {
                json += "\"" + entry.getValue().toString() + "\"";
            }
        }
        return json + "}";
    }
}
