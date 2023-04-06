package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;

public interface StackOverflowClient {
    Mono<StackOverflowResponse> getQuestions(long... ids);
}
