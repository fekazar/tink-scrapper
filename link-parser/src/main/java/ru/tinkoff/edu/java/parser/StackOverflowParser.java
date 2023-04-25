package ru.tinkoff.edu.java.parser;

import java.net.URL;

public class StackOverflowParser extends Parser {
    @Override
    protected String getHost() {
        return "stackoverflow.com";
    }

    @Override
    protected ParseResult process(URL url) {
        String[] path = url.getPath().split("/");

        if (path.length < 3)
            throw new RuntimeException("Incorrect stackoverflow reference path.");

        try {
            return new Result(Long.parseLong(path[2]));
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Stackoverflow question id is not numeric.");
        }
    }

    public record Result (long id) implements ParseResult { }
}
