package ru.tinkoff.edu.java.parser;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class StackOverflowParser extends Parser {
    public static final String STACK_OVER_ID = "id";

    @Override
    protected String getHost() {
        return "stackoverflow.com";
    }

    @Override
    protected ParseResult process(URL url) {
        String[] path = url.getPath().split("/");

        if (path.length < 3)
            throw new RuntimeException("Incorrect stackoverflow reference path.");

        Map<String, String> map = new TreeMap<>();
        map.put(STACK_OVER_ID, path[2]);

        return new ParseResult(map);
    }
}
