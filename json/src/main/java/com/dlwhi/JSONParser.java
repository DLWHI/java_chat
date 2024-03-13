package com.dlwhi;

public class JSONParser {
    private static JSONObject current;
    private static JSONLexer lexer;
    // TODO add array handling
    // TODO move this to json builder
    public static JSONObject parseString(String source) {
        JSONObject root = new JSONObject();
        current = root;
        lexer = new JSONLexer(source);
        try {
            lexer.getSymbol('{');
            for (; current != null;) {
                if (lexer.trySymbol('}')) {
                    current = current.getParent();
                    lexer.trySymbol(',');
                } else {
                    push();     
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidJSONException("Unexpected end of JSON object");
        }
        lexer = null;
        current = null;
        return root;
    }

    private static void push() {
        String key = lexer.getKey();
        String stringValue = lexer.getValue();
        if ("{".equals(stringValue)) {
            JSONObject obj = new JSONObject(current);
            current.add(key, obj);
            current = obj;
        } else if ("[".equals(stringValue)) {
            current.add(key, lexer.extractArray());
        } else {
            current.add(key, JSONLexer.stripValue(stringValue));
        }
    }
}
