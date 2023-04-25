package ru.tinkoff.edu.java.scrapper.repository.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class StackoverflowLink extends Link {

    @Setter
    @Getter
    private List<Answer> answers;

    public StackoverflowLink() {
    }
}
