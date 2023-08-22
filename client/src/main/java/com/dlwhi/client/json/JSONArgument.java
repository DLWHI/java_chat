package com.dlwhi.client.json;

public class JSONArgument {
    private String key;
    private String value;
    
    public JSONArgument(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s:\"%s\"", key, value);
    }
}
