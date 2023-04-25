package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

import java.util.List;

@Service("jdbcChatService")
public class JdbcChatService implements ChatService {
    @Autowired
    private JdbcScrapperRepository repository;

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
