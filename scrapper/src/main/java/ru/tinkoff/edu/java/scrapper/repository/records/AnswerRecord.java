package ru.tinkoff.edu.java.scrapper.repository.records;

// Class to represent records from answers table.

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerRecord {
    @JsonProperty("answer_id")
    private int answerId;

    @JsonIgnore
    private int linkId;

    public AnswerRecord(int answerId, int linkId) {
        this.answerId = answerId;
        this.linkId = linkId;
    }

    public AnswerRecord() {

    }

    @JsonProperty(value = "title", required = false)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerRecord that = (AnswerRecord) o;

        return answerId == that.answerId;
    }

    @Override
    public int hashCode() {
        return answerId;
    }
}
