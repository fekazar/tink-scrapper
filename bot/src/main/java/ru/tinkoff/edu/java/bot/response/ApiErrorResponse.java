package ru.tinkoff.edu.java.bot.response;

public record ApiErrorResponse(String description, long code, String exceptionName, String exceptionMessage) {
}
