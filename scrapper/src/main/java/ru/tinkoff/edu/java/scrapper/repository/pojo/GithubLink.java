package ru.tinkoff.edu.java.scrapper.repository.pojo;

import lombok.Getter;
import lombok.Setter;

public class GithubLink extends Link {
    @Getter
    @Setter
    private String pullsString;

    public GithubLink() {
        super();
    }
}
