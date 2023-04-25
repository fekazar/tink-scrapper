package ru.tinkoff.edu.java.scrapper.repository.pojo;

// Class to represent records from answers table.

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Answer {
    @JsonProperty("answer_id")
    private int answerId;

    @JsonIgnore
    private int linkId;

    public Answer() {
    }

    public Answer(int answerId, int linkId) {
        this.answerId = answerId;
        this.linkId = linkId;
    }

    @JsonProperty(value = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer that = (Answer) o;

        return answerId == that.answerId;
    }

    @Override
    public int hashCode() {
        return answerId;
    }
}
