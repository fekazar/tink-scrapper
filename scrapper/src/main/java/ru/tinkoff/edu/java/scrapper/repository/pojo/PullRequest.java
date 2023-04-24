package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "pull_requests")
@Getter
@Setter
public class PullRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    private Long linkId;
}
