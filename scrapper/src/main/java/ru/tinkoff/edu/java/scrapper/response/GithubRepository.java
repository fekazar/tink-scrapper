package ru.tinkoff.edu.java.scrapper.response;

import java.time.OffsetDateTime;

public record GithubRepository(OffsetDateTime updated_at) {
}
