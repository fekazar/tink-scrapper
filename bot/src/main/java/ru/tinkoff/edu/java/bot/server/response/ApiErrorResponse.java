package ru.tinkoff.edu.java.bot.server.response;

public record ApiErrorResponse(String description, long code, String exceptionName, String exceptionMessage) {
}
