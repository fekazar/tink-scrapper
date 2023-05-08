package ru.tinkoff.edu.java.parser;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTests {
    Parser parser;

    @BeforeAll
    void setup() {
        parser = new GithubParser();
        parser.setNext(new StackOverflowParser());
    }

    @Test
    void github1_Test() throws MalformedURLException {
        var res = (GithubParser.Result) parser.parse(new URL("https://github.com/sanyarnd/tinkoff-java-course-2022/"));

        assertNotNull(res);
        assertEquals("sanyarnd", res.user());
        assertEquals("tinkoff-java-course-2022", res.repository());
    }

    @Test
    void github2_Test() throws MalformedURLException {
        var res = (GithubParser.Result) parser.parse(new URL("https://github.com/User-FK/tink_java_proj"));

        assertNotNull(res);
        assertEquals("User-FK", res.user());
        assertEquals("tink_java_proj", res.repository());
    }

    @Test
    void bad_github_Test() throws MalformedURLException {
        assertThrows(RuntimeException.class, () -> parser.parse(new URL("https://github.com/fdsf")));
    }

    @Test
    void stackoverflow1_Test() throws MalformedURLException {
        var res = (StackOverflowParser.Result) parser.parse(new URL("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));

        assertNotNull(res);
        assertEquals(1642028, res.id());
    }

    @Test
    void stackoverflow2_Test() throws MalformedURLException {
        var res = (StackOverflowParser.Result) parser.parse(new URL("https://stackoverflow.com/questions/209555/why-would-you-choose-the-java-programming-language-over-others"));

        assertNotNull(res);
        assertEquals(209555, res.id());
    }

    @Test
    void bad_stackoverflow_Test() throws MalformedURLException {
        assertThrows(RuntimeException.class, () -> parser.parse(new URL("https://stackoverflow.com/search?q=unsupported%20link")));
        assertThrows(RuntimeException.class, () -> parser.parse(new URL("https://stackoverflow.com/questions/abacaba/what-is-the-operator-in-c")));
    }

    @Test
    void unsupported_link_Test() throws MalformedURLException {
        assertNull(parser.parse(new URL("https://test.com/search?q=unsupported%20link")));
    }
}
