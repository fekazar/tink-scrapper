package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.repository.records.AnswerRecord;

import java.util.List;

public class AnswersResponse {
    @JsonProperty("items")
    @Getter
    @Setter
    private List<AnswerRecord> answers;

    public AnswersResponse() {

    }
}
