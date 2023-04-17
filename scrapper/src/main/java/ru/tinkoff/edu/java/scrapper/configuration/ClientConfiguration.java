package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.response.GithubRepository;
import ru.tinkoff.edu.java.scrapper.response.PullsResponse;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;

@Slf4j
@Component
@PropertySource("classpath:secrets.properties")
public class ClientConfiguration {

    @Bean
    public GithubClient getGithubClient(@Value("${secrets.github_api_key}") String githubApiKey) {
        log.info("Github api token: " + githubApiKey);

        final var webClient = WebClient.builder()
                .defaultHeader("authorization", "Bearer " + githubApiKey)
                .baseUrl("https://api.github.com/repos/")
                .build();

        return (user, repositoryName) ->  {
            PullsResponse[] pulls = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(user)
                            .pathSegment(repositoryName)
                            .pathSegment("pulls")
                            .queryParam("state", "all")
                            .build())
                    .retrieve()
                    .bodyToMono(PullsResponse[].class)
                    .block();

            var githubRepo = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(user)
                            .pathSegment(repositoryName)
                            .build())
                    .retrieve()
                    .bodyToMono(GithubRepository.class)
                    .block();

            githubRepo.setPulls(pulls);
            return githubRepo;
        };

    }

    @Bean
    public StackOverflowClient getStackOverflowclient() {
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

    @Bean
    public Parser getLinkParser() {
        var parser = new GithubParser();
        parser.setNext(new StackOverflowParser());
        return parser;
    }
}
