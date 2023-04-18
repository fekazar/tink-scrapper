package ru.tinkoff.edu.java.scrapper.repository.records;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class StackoverflowLinkRecord extends LinkRecord {

    @Setter
    @Getter
    private List<AnswerRecord> answers;
}
