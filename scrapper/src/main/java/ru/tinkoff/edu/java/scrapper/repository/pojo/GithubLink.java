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
@DiscriminatorValue("github")
@Getter
@Setter
public class GithubLink extends Link {
    // TODO: make a one 2 many relationship with PullRequest entity

    private String pullsString;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "link_id")
    private List<PullRequest> pullRequests = new ArrayList<>();

    public GithubLink() {
        super();
    }
}
