package ru.tinkoff.edu.java.parser;

import java.net.URL;
import java.util.ArrayList;

// This class is needed only for first, primitive tests.
// TODO: remove later
public class Main {
    public static void main(String[] args) throws Exception {
        var toProcess = new ArrayList<URL>() {{
            add(new URL("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
            add(new URL("https://github.com/User-FK/tink_java_proj"));
            add(new URL("https://github.com/fdsf"));
            add(new URL("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));
            add(new URL("https://stackoverflow.com/questions/209555/why-would-you-choose-the-java-programming-language-over-others"));
            add(new URL("https://stackoverflow.com/search?q=unsupported%20link"));
            add(new URL("https://stackoverflow.com/questions/abacaba/what-is-the-operator-in-c"));
            add(new URL("https://test.com/search?q=unsupported%20link"));
        }};

        Parser parser = new GithubParser();
        parser.setNext(new StackOverflowParser());

        for (URL url: toProcess) {
            try {
                System.out.println(parser.parse(url));
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Bad url");
            }
        }
    }
}
