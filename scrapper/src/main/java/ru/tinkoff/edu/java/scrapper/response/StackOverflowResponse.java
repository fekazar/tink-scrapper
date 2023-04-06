package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(List<Question> items) {
    public static record Question(String title, @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate) {}
}
