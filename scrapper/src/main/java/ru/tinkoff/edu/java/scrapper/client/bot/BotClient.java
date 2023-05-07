package ru.tinkoff.edu.java.scrapper.client.bot;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BotClient {
    void sendUpdates(HttpBotClient.LinkUpdate body);

    record LinkUpdate(@JsonProperty("text_message") String textMessage,
                      String url,
                      @JsonProperty("tg_chat_id") long tgChatId) { }
}
