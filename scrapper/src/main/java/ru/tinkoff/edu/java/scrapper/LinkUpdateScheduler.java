package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.bot.HttpBotClient;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.*;

@Slf4j
@Component
public class LinkUpdateScheduler {
    @Autowired
    @Qualifier("linkProcessorMap")
    private Map<String, LinkProcessor> processorMap;

    @Autowired
    private LinkService linkService;

    @Autowired
    private HttpBotClient botClient;

    @Scheduled(fixedRateString = "#{@getRateMillis}")
    void update() {
        List<Link> toUpdate = linkService.getAll();
        Map<String, ArrayList<Link>> chatsByUrls = new TreeMap<>();

        for (var linkRec: toUpdate) {
            if (!chatsByUrls.containsKey(linkRec.getUrl()))
                chatsByUrls.put(linkRec.getUrl(), new ArrayList<>());

            chatsByUrls.get(linkRec.getUrl()).add(linkRec);
        }

        for (String url: chatsByUrls.keySet()) {
            log.info("Scheduler processing: " + url);

            try {
                Link toProcess = chatsByUrls.get(url).get(0);
                // TODO: make async
                var result = linkService.process(toProcess);

                if (result.hasChanges()) {
                    log.info("Some changes at: " + url);

                    // Are "unsafe" entities used in this list? Shouldn't there be an updated link after saving to database?
                    for (var linkRec : chatsByUrls.get(url)) {
                        sendMessage(new HttpBotClient.LinkUpdate(result.getDescription(), url, linkRec.getChatId()));
                        log.info("Sending message to: " + linkRec.getChatId());
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
    void sendMessage(HttpBotClient.LinkUpdate body) {
        try {
            botClient.sendUpdates(body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
