package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.util.List;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@AllArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    public void add(long chatId) {
        chatRepository.save(new Chat(chatId));
    }

    @Override
    public void remove(long chatId) {
        chatRepository.deleteById(chatId);
    }

    @Override
    public List<Chat> getAll() {
        return chatRepository.findAll();
    }
}
