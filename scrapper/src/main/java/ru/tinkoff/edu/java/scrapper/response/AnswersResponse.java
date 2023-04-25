package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Answer;

import java.util.List;

public class AnswersResponse {
    @JsonProperty("items")
    @Getter
    @Setter
    private List<Answer> answers;

    public AnswersResponse() {

    }
}
