package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("stackoverflow")
@Getter
@Setter
public class StackoverflowLink extends Link {

    @Setter
    @Getter
    @OneToMany(fetch = FetchType.EAGER) // on delete cascade?
    @JoinColumn(name = "link_id")
    private List<Answer> answers = new ArrayList<>();

    public StackoverflowLink() {
        super();
        hostType = "stackoverflow";
    }
}
