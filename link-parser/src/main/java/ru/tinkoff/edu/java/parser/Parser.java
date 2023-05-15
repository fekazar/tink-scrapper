package ru.tinkoff.edu.java.parser;

import java.net.URL;

public abstract class Parser {
    protected Parser next;

    final public ParseResult parse(URL url) {
        if (!url.getHost().equals(getHost())) {
            if (next == null)
                return null;

            return next.parse(url);
        }

        return process(url);
    }

    public Parser setNext(Parser next) {
        this.next = next;
        return next;
    }

    protected abstract String getHost();

    protected abstract ParseResult process(URL url);
}
