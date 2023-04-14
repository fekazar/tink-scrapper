package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.JdbcScrapperRepository;

//@Service
@Component
public class JdbcChatService {
    @Autowired
    private JdbcScrapperRepository repository;

    public void add(int chatId) {
        repository.addChat(chatId);
    }

    public void remove(int chatId) {
        repository.deleteChat(chatId);
    }
}
