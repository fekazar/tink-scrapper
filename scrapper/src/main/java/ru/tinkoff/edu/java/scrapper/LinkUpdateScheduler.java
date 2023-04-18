package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.urlprocessor.UrlProcessor;
import ru.tinkoff.edu.java.scrapper.repository.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URL;
import java.util.*;

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

        for (String url: chatsByUrls.keySet()) {
            log.info("Scheduler processing: " + url);

            try {
                LinkRecord toProcess = chatsByUrls.get(url).get(0);
                // TODO: make async
                var result = processorMap.get(new URL(url).getHost()).process(toProcess);

                if (result.hasChanges()) {
                    log.info("Some changes at: " + url);

                    for (var linkRec : chatsByUrls.get(url)) {
                        sendMessage(new BotClient.RequestBody(result.getDescription(), url, linkRec.chatId()));
                        log.info("Sending message to: " + linkRec.chatId());
                    }
                } else {
                    log.info("No changes at: " + url);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
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
