package com.dlwhi;

import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;
import java.text.DecimalFormatSymbols;

public class JSONBuilder {
    private static char cursor;
    private static CharBuffer sourceView;
    // TODO add array handling
    // TODO move this to json builder
    public static JSONPackage parseString(String source) {
        if (!source.startsWith("{")) {
            throw new InvalidJSONException("Expected json object at the start");            
        }
        int depth = 1;
        JSONPackage root = new JSONPackage();
        JSONPackage current = root;
        sourceView = CharBuffer.wrap(source);
        cursor = sourceView.get();
        while (sourceView.position() != sourceView.limit()) {
            while (Character.isWhitespace((cursor = sourceView.get())));
            String key = getKey();
            if ((cursor = sourceView.get()) != ':') {
                throw new InvalidJSONException("Expected \":\" char after key");
            }
            Object value = getValue(current);
            current.add(key, value);
            if (cursor == '}') {
                current = current.getParent();
            }
            entryCleanUp();
        }
        return root;
    }

    private static String getKey() {
        CharBuffer start = sourceView.slice();
        int startInd = sourceView.position();
        while ((cursor = sourceView.get()) != '\"');
        return start.limit(sourceView.position() - startInd - 1).toString();
    }

    private static Object getValue(JSONPackage parent) {
        while (Character.isWhitespace((cursor = sourceView.get())));
        CharBuffer start = sourceView.slice();
        int startInd = sourceView.position();
        if (cursor != '\"') {
            return getNumeric();
        } else if (cursor == '{') {
            return new JSONPackage(parent);
        } else {
            while ((cursor = sourceView.get()) != '\"');
            cursor = sourceView.get();
            return start.limit(sourceView.position() - startInd - 2).toString();
        }
    }

    private static Object getNumeric() {
        double num = cursor - '0';
        double mantissa = 0;
        while (Character.isDigit((cursor = sourceView.get()))) {
            num = num*10 + cursor - '0';
        }
        if (cursor == '.') {
            mantissa = 1;
            while (Character.isDigit((cursor = sourceView.get()))) {
                num = num*10 + cursor - '0';
                mantissa *= 10;
            }
        }
        if (mantissa == 0) {
            return (int) num;
        }
        return num;
    }

    private static void entryCleanUp() {
        if (Character.isWhitespace(cursor)) {
            while (Character.isWhitespace((cursor = sourceView.get())));
        }
        if (cursor != ',' && cursor != '}') {
            throw new InvalidJSONException("Expected entries to be separated by comma");
        }
    }

    private static JSONPackage dive(String childName, JSONPackage current) {
        JSONPackage child = new JSONPackage(current);
        current.add(childName, child);
        return child;
    }

    private static JSONPackage rise(JSONPackage current) {
        return current.getParent();
    }

    private void queue(String element, JSONPackage current) {
        // if (queue.size() == 1) {
        //     current.add(queue.poll(), element);
        // } else {
        //     queue.add(element);
        // }
    }
}
