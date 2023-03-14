package ru.tinkoff.edu.java.parser;

import java.util.Map;
import java.util.TreeMap;

public class ParseResult {
    private final Map<String, String> result;
    public ParseResult(Map<String, String> map) {
        result = new TreeMap<>(map);
    }

    public String get(String key) {
        if (!result.containsKey(key))
            throw new RuntimeException("Unsupported result key.");

        return result.get(key);
    }
}
