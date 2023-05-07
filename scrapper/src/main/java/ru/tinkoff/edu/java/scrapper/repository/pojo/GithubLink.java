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

    @Transient
    private String pullsString;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "link_id")
    private List<PullRequest> pullRequests = new ArrayList<>();

    public GithubLink() {
        super();
        hostType = "github";
    }
}
