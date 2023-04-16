package ru.tinkoff.edu.java.scrapper;

import io.swagger.v3.oas.models.links.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.urlprocessor.UrlProcessor;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LinkUpdateScheduler {
    @Autowired
    private Map<String, UrlProcessor> processorMap;

    @Autowired
    private LinkService linkService;

    @Autowired
    private BotClient botClient;

    @Scheduled(fixedRateString = "#{@getRateMillis}")
    void update() {
        List<LinkRecord> toUpdate = linkService.getAll();
        Map<String, ArrayList<LinkRecord>> chatsByUrls = new TreeMap<>();

        for (var linkRec: toUpdate) {
            if (!chatsByUrls.containsKey(linkRec.url()))
                chatsByUrls.put(linkRec.url(), new ArrayList<>());

            chatsByUrls.get(linkRec.url()).add(linkRec);
        }

        try {
            for (String url: chatsByUrls.keySet()) {
                LinkRecord toProcess = chatsByUrls.get(url).get(0);
                // TODO: make async
                var result = processorMap.get(new URL(url).getHost()).process(toProcess);
                linkService.setLastUpdate(url, result.linkRecord().lastUpdate());

                for (var linkRec: chatsByUrls.get(url)) {
                    if (result.linkRecord() != linkRec) {
                        sendMessage(new BotClient.RequestBody(result.description(), url, linkRec.chatId()));
                        log.info("Sending message to: " + linkRec.chatId());
                    } else {
                        log.info("No changes at: " + url);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Async
    void sendMessage(BotClient.RequestBody body) {
        try {
            botClient.sendUpdates(body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
