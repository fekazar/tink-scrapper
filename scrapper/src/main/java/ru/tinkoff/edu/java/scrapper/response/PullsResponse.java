package ru.tinkoff.edu.java.scrapper.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PullsResponse {
    private String url;
    private String title;

    @Getter
    public static class User {
        private String login;
    }
}
