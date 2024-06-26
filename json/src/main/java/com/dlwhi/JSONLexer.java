package com.dlwhi;

import java.util.ArrayList;

public class JSONLexer {
    private final String source;
    private int position;

    public JSONLexer(String source) {
        this.source = source;
    }

    public char getSymbol(char expected) {
        skipSpaces();
        if (source.charAt(position) == expected) {
            position++;
            return expected;
        }
        throw new InvalidJSONException("Invalid symbol '" + source.charAt(position) + "' at [:" + position + "]");
    }

    public boolean trySymbol(char symbol) {
        skipSpaces();
        if (position < source.length() && source.charAt(position) == symbol) {
            position++;
            return true;
        }
        return false;
    }

    public String getKey() {
        getSymbol('"');
        String val = nextString();
        getSymbol(':'); 
        return val;
    }

    public String getValue() {
        skipSpaces();
        char cursor = source.charAt(position);
        String val;
        if (cursor == '"') {
            ++position;
            val = nextString();
        } else {
            val = sliceValue();
        }
        skipSpaces();
        if (!trySymbol(',') && "}".indexOf(source.charAt(position)) == -1) {
            throw new InvalidJSONException("Expected values to be comma separated [:" + position + "]");
        }
        return val;
    }

    public Object[] extractArray() {
        ArrayList<Object> arr = new ArrayList<>();
        while (!trySymbol(']')) {
            arr.add(stripValue(getValue()));
        }
        if (!trySymbol(',') && "}]".indexOf(source.charAt(position)) == -1) {
            throw new InvalidJSONException("Expected values to be comma separated [:" + position + "]");
        }
        return arr.toArray();
    }

    private String nextString() {
        int start = position;
        for (; source.charAt(position) != '"'; ++position)
        ;
        String str = source.substring(start, position++);
        return str;
    }

    private String sliceValue() {
        int start = position;
        char cursor = source.charAt(position++);
        for (; !Character.isWhitespace(cursor) && "},".indexOf(cursor) == -1; ++position) {
            cursor = source.charAt(position);
        }
        return source.substring(start, --position);
    }

    public static Object stripValue(String value) {
        if (!value.isEmpty() && Character.isDigit(value.charAt(0))) {
            Double val = Double.parseDouble(value);
            double valStripped = Math.rint(val);
            if (val == valStripped) {
                return Integer.valueOf((int)valStripped);
            }
            return val;
        } else if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        } else {
            return value;
        }
    }

    private void skipSpaces() {
        for (; position < source.length() && Character.isWhitespace(source.charAt(position)); ++position)
            ;
    }
}
