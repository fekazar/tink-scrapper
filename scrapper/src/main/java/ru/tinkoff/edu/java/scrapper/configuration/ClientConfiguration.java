package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.response.GithubRepository;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;

@Component
public class ClientConfiguration {

    @Bean
    GithubClient getGithubClient() {
        final var webClient = WebClient.builder()
                .baseUrl("https://api.github.com/repos/")
                .build();

        return (user, repositoryName) -> webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(user)
                        .pathSegment(repositoryName)
                        .build())
                .retrieve()
                .bodyToMono(GithubRepository.class);
    }

    @Bean
    StackOverflowClient getStackOverflowclient() {
        final var httpClient = HttpClient.create()
                .baseUrl("https://api.stackexchange.com/2.3/questions")
                .compress(true);

        final var webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return ids -> webClient.get()
                .uri(uriBuilder -> {
                    for (long id: ids)
                        uriBuilder.pathSegment(String.valueOf(id));

                    uriBuilder.queryParam("site", "stackoverflow");
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(StackOverflowResponse.class);
    }
}
