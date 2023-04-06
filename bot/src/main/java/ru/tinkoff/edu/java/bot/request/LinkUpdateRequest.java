package ru.tinkoff.edu.java.bot.request;

public record LinkUpdateRequest(long id, String url, String description, long[] tgChatIds) {
}
