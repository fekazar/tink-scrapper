package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
    @OneToMany() // on delete cascade?
    @JoinColumn(name = "link_id")
    private List<Answer> answers = new ArrayList<>();

    public StackoverflowLink() {
        super();
        hostType = "stackoverflow";
    }
}
