package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JSONParserTest {
    @Test
    public void trivial() {
        String json = "{entry1: \"data1\", entry2: \"data2\", entry3: \"data3\"}";
        JSONPackage parsed = JSONPackage.fromString(json);
        String toStringExpected = "{entry1: \"data1\", entry2: \"data2\", entry3: \"data3\",}";
        assertEquals(toStringExpected, parsed.toString());
    }

    @Test
    public void classic() {
        String json = "{entry1: \"data1\", entry2: \"data2\", entry3: \"data3\",}";
        JSONPackage parsed = JSONPackage.fromString(json);
        assertEquals(json, parsed.toString());
    }

    @Test
    public void trivialMultiLine() {
        String json = "{\n" + 
            "entry1: \"data1\",\n" +
            "entry2: \"data2\",\n" +
            "entry3: \"data3\"\n" + 
            "}";
        JSONPackage parsed = JSONPackage.fromString(json);
        assertEquals("data1", parsed.get("entry1").toString());
        assertEquals("data2", parsed.get("entry2").toString());
        assertEquals("data3", parsed.get("entry3").toString());
    }
}
