package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GithubRepository(@JsonProperty("updated_at") OffsetDateTime updatedAt) {
    public String getDescription() {
        var sb = new StringBuilder();
        sb.append("Updated at: ").append(updatedAt);
        return sb.toString();
    }
}
