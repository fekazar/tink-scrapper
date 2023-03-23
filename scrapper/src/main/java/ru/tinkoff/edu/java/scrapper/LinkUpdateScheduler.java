package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;

@Slf4j
@Component
public class LinkUpdateScheduler {
    @Autowired
    private GithubClient githubClient;

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Scheduled(fixedRateString = "#{@getRateMillis}")
    void update() {
        // For now there are hard-coded values
        // TODO: make a repository

        log.info("Updating data from clients...");

        githubClient.getRepository("User-FK", "tink_java_proj")
                .subscribe(githubClient  -> {
                    log.info("Github updated at: " + githubClient.updated_at());
                });

        stackOverflowClient.getQuestions(4421706)
                .subscribe(stackOverflowResponse -> {
                    for (var question: stackOverflowResponse.items())
                        log.info("Question: " + question);
                });
    }
}
