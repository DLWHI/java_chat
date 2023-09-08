package com.dlwhi;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
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

    // TODO add array handling
    public static JSONPackage fromString(String source) throws InvalidJSONException {
        JSONPackage root = new JSONPackage();
        int depth = 1;
        try (Scanner parser = new Scanner(source)) {
            parser.useDelimiter("(?:[\r\n:,\\\"]| \\\")+").skip("\\{");
            JSONPackage current = root;
            while(depth != 0) {
                if (parser.hasNext("\\{")) {
                    JSONPackage child = new JSONPackage(current);
                    current = child;
                    ++depth;
                    parser.skip("\\{");
                } else if (parser.hasNext("\\}")) {
                    current = current.getParent();
                    --depth;
                    parser.skip("[ ,\n\r\"]*\\}");
                } else if (parser.hasNext()) {
                    current.add(parser.next(), parser.next());
                }
            }
        } catch (NoSuchElementException e) {
            throw new InvalidJSONException(e.getMessage());
        }
        return root;
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

    public int getAsInt(String param) {
        return Integer.valueOf(getAsInt(param));
    }

    public JSONPackage getParent() {
        return parent;
    }

    @Override
    public String toString() {
        String json = "{";
        Set<Map.Entry<String, Object>> entries = args.entrySet();
        Iterator<Map.Entry<String, Object>> i = entries.iterator();
        Map.Entry<String, Object> entry;
        if (i.hasNext()) {
            entry = i.next();
            json += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
        }
        while (i.hasNext()) {
            entry = i.next();
            json += ",";
            json += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
        }
        return json + "}";
    }
}
