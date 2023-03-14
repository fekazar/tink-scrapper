package ru.tinkoff.edu.java.parser;

import java.net.URL;
import java.util.TreeMap;
import java.util.Map;

public class GithubParser extends Parser {
    public static final String USER_KEY = "user";
    public static final String REP_KEY = "repos";

    @Override
    protected String getHost() {
        return "github.com";
    }

    @Override
    protected ParseResult process(URL url) {
        Map<String, String> map = new TreeMap<>();
        String[] path = url.getPath().split("/");

        if (path.length < 3)
            throw new RuntimeException("Incorrect github reference path.");

        map.put(USER_KEY, path[1]);
        map.put(REP_KEY, path[2]);

        return new ParseResult(map);
    }
}
