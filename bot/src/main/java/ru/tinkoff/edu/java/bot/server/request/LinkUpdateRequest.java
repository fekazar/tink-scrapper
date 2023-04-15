package ru.tinkoff.edu.java.bot.server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkUpdateRequest(String url,
                                @JsonProperty("text_message") String textMessage,
                                @JsonProperty("tg_chat_id") long tgChatId) {
}
