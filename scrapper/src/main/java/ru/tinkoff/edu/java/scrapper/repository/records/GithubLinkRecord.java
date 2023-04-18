package ru.tinkoff.edu.java.scrapper.repository.records;

import lombok.Getter;
import lombok.Setter;

public class GithubLinkRecord extends LinkRecord {
    @Getter
    @Setter
    private String pullsString;

    public GithubLinkRecord() {
        super();
    }
}
