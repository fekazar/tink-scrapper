package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
        hostType = "github";
    }
}
