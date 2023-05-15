package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.util.List;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@AllArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcScrapperRepository repository;

    public void add(long chatId) {
        repository.addChat(chatId);
    }

    public void remove(long chatId) {
        repository.deleteChat(chatId);
    }

    public List<Chat> getAll() {
        return repository.getAllChats();
    }
}
