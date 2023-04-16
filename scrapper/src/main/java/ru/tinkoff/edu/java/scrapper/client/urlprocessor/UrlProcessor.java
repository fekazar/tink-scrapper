package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

public interface UrlProcessor {
    public static final String THREAD_POOL = "url_processor_pool";
    Result process(LinkRecord linkRecord);

    public record Result(LinkRecord linkRecord, String description) {}
}
