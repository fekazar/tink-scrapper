package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.records.ChatRecord;

import java.util.List;

public interface ChatService {
    void add(long chatId);
    void remove(long chatId);
    List<ChatRecord> getAll();
}
