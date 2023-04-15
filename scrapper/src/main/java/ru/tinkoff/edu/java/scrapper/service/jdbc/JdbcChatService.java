package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.ChatRecord;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

import java.util.List;

@Service
public class JdbcChatService implements ChatService {
    @Autowired
    private JdbcScrapperRepository repository;

    public void add(int chatId) {
        repository.addChat(chatId);
    }

    public void remove(int chatId) {
        repository.deleteChat(chatId);
    }

    public List<ChatRecord> getAll() {
        return repository.getAllChats();
    }
}
