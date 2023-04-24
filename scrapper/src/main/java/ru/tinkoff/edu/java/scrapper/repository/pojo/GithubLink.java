package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("github")
@Getter
@Setter
public class GithubLink extends Link {
    // TODO: make a one 2 many relationship with PullRequest entity

    @Transient
    private String pullsString;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "link_id")
    private List<PullRequest> pullRequests = new ArrayList<>();

    public GithubLink() {
        super();
        hostType = "github";
    }
}
