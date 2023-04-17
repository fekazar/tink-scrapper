package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GithubRepository(@JsonProperty("pushed_at") OffsetDateTime pushedAt) {
    public String getDescription() {
        var sb = new StringBuilder();
        sb.append("Pushed at: ").append(pushedAt);
        return sb.toString();
    }
}
