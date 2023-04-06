package ru.tinkoff.edu.java.scrapper.response;

public record ApiErrorResponse(String description, long code, String exceptionName, String exceptionMessage) {
}
