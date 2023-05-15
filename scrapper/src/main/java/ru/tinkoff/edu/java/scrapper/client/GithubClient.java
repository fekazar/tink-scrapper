package ru.tinkoff.edu.java.scrapper.client;

import ru.tinkoff.edu.java.scrapper.response.GithubRepository;

public interface GithubClient {
    GithubRepository getRepository(String user, String repositoryName);
}
