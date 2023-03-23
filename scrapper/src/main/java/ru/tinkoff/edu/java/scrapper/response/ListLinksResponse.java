package ru.tinkoff.edu.java.scrapper.response;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, long size) {
}
