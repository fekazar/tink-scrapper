package ru.tinkoff.edu.java.parser;

import java.net.URL;

public class StackOverflowParser extends Parser {
    private static final int MIN_LENGTH = 3;

    @Override
    protected String getHost() {
        return "stackoverflow.com";
    }

    @Override
    protected ParseResult process(URL url) {
        String[] path = url.getPath().split("/");

        if (path.length < MIN_LENGTH)
            throw new RuntimeException("Incorrect stackoverflow reference path.");

        try {
            return new Result(Long.parseLong(path[2]));
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Stackoverflow question id is not numeric.");
        }
    }

    public record Result(long id) implements ParseResult { }
}
