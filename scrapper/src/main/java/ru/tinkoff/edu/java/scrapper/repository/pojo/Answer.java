package ru.tinkoff.edu.java.scrapper.repository.pojo;

// Class to represent records from answers table.

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "stackoverflow_answers")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Answer {
    @JsonProperty("answer_id")
    @Id
    @Column(name = "answer_id")
    private Long answerId;

    @JsonIgnore
    @Column(name = "link_id")
    private Long linkId;

    public Answer() {
    }

    public Answer(Long answerId, Long linkId) {
        this.answerId = answerId;
        this.linkId = linkId;
    }

    @JsonProperty(value = "title")
    @Transient // it's not required, but can be added in future
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer that = (Answer) o;

        return answerId.equals(that.answerId);
    }

    @Override
    public int hashCode() {
        return answerId != null ? answerId.hashCode() : 0;
    }
}
