package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// TODO: change fetch of answers to lazy

@Entity
@DiscriminatorValue("stackoverflow")
@Getter
@Setter
public class StackoverflowLink extends Link {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "link_id")
    private List<Answer> answers = new ArrayList<>();

    public StackoverflowLink() {
        super();
        hostType = "stackoverflow";
    }
}
