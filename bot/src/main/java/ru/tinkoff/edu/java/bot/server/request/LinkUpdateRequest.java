package ru.tinkoff.edu.java.bot.server.request;

public record LinkUpdateRequest(long id, String url, String description, long[] tgChatIds) {
}
