package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JSONParserTest {
    @Test
    public void trivial() {
        String json = "{\n" +
            "\"entry1\":\"data1\",\n" +
            "\"entry2\":\"data2\",\n" +
            "\"entry3\":\"data3\"\n" +
        "}";
        JSONObject parsed = JSONObject.fromString(json);
        String toStringExpected = "{\"entry1\":\"data1\",\"entry2\":\"data2\",\"entry3\":\"data3\"}";
        assertEquals(toStringExpected, parsed.toString());
        assertEquals("data1", parsed.get("entry1").toString());
        assertEquals("data2", parsed.get("entry2").toString());
        assertEquals("data3", parsed.get("entry3").toString());
    }

    @Test
    public void classic() {
        String json = "{\"entry1\":\"data1\",\"entry2\":\"data2\",\"entry3\":\"data3\"}";
        JSONObject parsed = JSONObject.fromString(json);
        assertEquals(json, parsed.toString());
        assertEquals("data1", parsed.get("entry1").toString());
        assertEquals("data2", parsed.get("entry2").toString());
        assertEquals("data3", parsed.get("entry3").toString());
    }

    @Test
    public void empty() {
        String json = "{}";
        JSONObject parsed = JSONObject.fromString(json);
        assertEquals(json, parsed.toString());
        assertEquals(0, parsed.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/array.json",
        "/combined.json",
        "/nested.json",
        "/one_line.json",
        "/valid_multitype.json",
        "/valid.json"
    })
    public void fromFiles(String jsonFile) {
        final String jsonString = readFile(jsonFile);
        JSONObject json = assertDoesNotThrow(() -> JSONObject.fromString(jsonString));
        assertEquals(jsonString.replaceAll("\\s+", ""), json.toString());
    }

    private String readFile(String file) {
        String content = null;
        try (Scanner reader = new Scanner(getClass().getResourceAsStream(file))) {
            reader.useDelimiter("\\Z");  
            content = reader.next();
        }
        return content;
    }
}
