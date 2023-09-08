package com.dlwhi;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
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
    // TODO move this to json builder
    public static JSONPackage fromString(String source) throws InvalidJSONException {
        JSONPackage root = new JSONPackage();
        int depth = 1;
        try (Scanner parser = getJSONReader(source)) {
            JSONPackage current = root;
            Queue<String> queue = new LinkedList<>();
            while(depth != 0) {
                String element = parser.next();
                if (element.charAt(0) == '{') {
                    current = current.dive(element);
                    depth++;
                } else if (element.charAt(0) == '}') {
                    current = current.getParent();
                    depth--;
                } else {
                    if (queue.size() == 1) {
                        current.add(queue.poll(), element);
                    } else {
                        queue.add(element);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw new InvalidJSONException(e.getMessage());
        }
        return root;
    }

    private static Scanner getJSONReader(String source) {
        return new Scanner(source).useDelimiter("[\\s,\":]+").skip("\\{");
    }

    private JSONPackage dive(String childName) {
        JSONPackage child = new JSONPackage(this);
        add(childName, child);
        return child;
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
