package ru.tinkoff.edu.java.scrapper.service;

import java.util.List;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;

public interface ChatService {
    void add(long chatId);

    void remove(long chatId);

    List<Chat> getAll();
}
