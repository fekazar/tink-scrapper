package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(List<Question> items) {

    public String getSingleQuestionDescription() {
        var sb = new StringBuilder();
        sb.append("Question updated: ").append(items.get(0).title).append(" ").append(items.get(0).lastActivityDate());
        return sb.toString();
    }

    public static record Question(String title, @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate) {}
}
