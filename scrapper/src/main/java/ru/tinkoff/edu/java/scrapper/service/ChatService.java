package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.ChatRecord;

import java.util.List;

public interface ChatService {
    void add(int chatId);
    void remove(int chatId);
    List<ChatRecord> getAll();
}
