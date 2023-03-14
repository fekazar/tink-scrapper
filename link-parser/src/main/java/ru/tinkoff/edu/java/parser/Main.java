package ru.tinkoff.edu.java.parser;

import java.net.MalformedURLException;
import java.net.URL;

// This class is needed only for first, primitive tests.
// TODO: remove later
public class Main {
    public static void main(String[] args) {
        Parser parser = new GithubParser();
        parser.setNext(new StackOverflowParser());

        try {
            URL url = new URL("https://github.com/sanyarnd/tinkoff-java-course-2022/");
            ParseResult res = parser.parse(url);

            System.out.println(res.get(GithubParser.USER_KEY));
            System.out.println(res.get(GithubParser.REP_KEY));
        } catch (MalformedURLException me) {
            System.out.println("Bad client url");
        }

        try {
            URL url = new URL("https://github.com/User-FK/tink_java_proj");
            ParseResult res = parser.parse(url);

            System.out.println(res.get(GithubParser.USER_KEY));
            System.out.println(res.get(GithubParser.REP_KEY));
        } catch (MalformedURLException me) {
            System.out.println("Bad client url");
        }

        try {
            URL url = new URL("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            var res = parser.parse(url);

            System.out.println(res.get(StackOverflowParser.STACK_OVER_ID));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            URL url = new URL("https://stackoverflow.com/questions/209555/why-would-you-choose-the-java-programming-language-over-others");
            var res = parser.parse(url);

            System.out.println(res.get(StackOverflowParser.STACK_OVER_ID));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            URL url = new URL("https://stackoverflow.com/search?q=unsupported%20link");
            var res = parser.parse(url);

            System.out.println(res.get(StackOverflowParser.STACK_OVER_ID));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            URL url = new URL("https://test.com/search?q=unsupported%20link");
            var res = parser.parse(url);
            System.out.println(res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
