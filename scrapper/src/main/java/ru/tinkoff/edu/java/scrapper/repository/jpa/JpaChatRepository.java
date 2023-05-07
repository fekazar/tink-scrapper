package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.repository.ListCrudRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;

// Default methods should be enough

public interface JpaChatRepository extends ListCrudRepository<Chat, Long> {
}
