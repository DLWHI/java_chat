package com.dlwhi.client.config;

import java.io.IOException;
import java.io.InputStreamReader;

import com.dlwhi.client.chat.Command;
import com.dlwhi.client.view.Menu;

public class MenuCfgParser {
    private final InputStreamReader reader;

    public MenuCfgParser(InputStreamReader reader) {
        this.reader = reader;
    }

    public void parseContext(String menuName, Menu ctx) throws IOException {
        String token;
        for (token = read() ;!token.isEmpty(); token = read()) {
            if (token.equals(menuName)) {
                parseInnerBlock(ctx);                
            } else {
                skip();
            }
        }
    }

    private void parseInnerBlock(Menu ctx) throws IOException {
        String token = read();
        expect(!"{".equals(token), "Context configuration must be in braces");
        for (token = read(); !"}".equals(token); token = read()) {
            int i = 0;
            int bind = 0;
            expect(token.charAt(i++) != '[', "Expected index in square braces");
            for (; i < token.length() - 1; i++) {
                bind *= 10;
                bind += token.charAt(i) - '0';
            }
            expect(token.charAt(i) != ']', "Expected index in square braces");
            ctx.addLine(bind, readUntil(':'));
            ctx.addCommand(bind, Command.fromString(readUntil('\n')));
        }
    }

    private String read() throws IOException {
        String tkn = "";
        int next;
        for (; Character.isWhitespace(next = reader.read()););
        for (; next != -1 && !Character.isWhitespace(next); next = reader.read()) {
            tkn += (char) next;
        }
        return tkn;
    }

    private String readUntil(char stop) throws IOException {
        String tkn = "";
        int next;
        for (; Character.isWhitespace(next = reader.read()););
        if (next < 0) {
            return tkn;
        }
        for (; next != -1 && next != stop; next = reader.read()) {
            tkn += (char) next;
        }
        return tkn;
    }

    private boolean skip() throws IOException {
        int next;
        for (; (next = reader.read()) != '}';);
        return next == -1;
    }

    private void expect(boolean expr, String msg) {
        if (expr) {
            throw new RuntimeException(msg);
        }
    }
}
