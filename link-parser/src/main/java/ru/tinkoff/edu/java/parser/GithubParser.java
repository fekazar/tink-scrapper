package ru.tinkoff.edu.java.parser;

import java.net.URL;

public class GithubParser extends Parser {
    @Override
    protected String getHost() {
        return "github.com";
    }

    @Override
    protected ParseResult process(URL url) {
        String[] path = url.getPath().split("/");

        if (path.length < 3)
            throw new RuntimeException("Incorrect github reference path.");

        return new Result(path[1], path[2]);
    }

    public record Result(String user, String repository) implements ParseResult { }
}
