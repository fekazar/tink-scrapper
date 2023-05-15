package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
        hostType = "stackoverflow";
    }
}
