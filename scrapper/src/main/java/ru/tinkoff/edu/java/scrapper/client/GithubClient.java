package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.response.GithubRepository;

public interface GithubClient {
    Mono<GithubRepository> getRepository(String user, String repositoryName);
}
