package ru.tinkoff.edu.java.bot.client.dto;

public record ApiErrorResponse(String description, long code, String exceptionName, String exceptionMessage) {
}
