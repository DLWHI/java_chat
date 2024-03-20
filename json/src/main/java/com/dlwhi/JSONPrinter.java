package com.dlwhi;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class JSONPrinter {
    public static String printEntry(String key, Object value) {
        String entry = '"' + key + "\":";
        if (value instanceof Object[]) {
            entry += '[';
            entry += Stream.of((Object[]) value).collect(Collectors.mapping(JSONPrinter::wrapValue, Collectors.joining(",")));
            entry += ']';
        } else {
            entry += wrapValue(value);
        }
        return entry;
    }

    private static String wrapValue(Object value) {
        if (value instanceof Number || value instanceof JSONObject || value instanceof Boolean) {
            return value.toString();
        } else {
            return '"' + value.toString() + '"';
        }
    }
}
