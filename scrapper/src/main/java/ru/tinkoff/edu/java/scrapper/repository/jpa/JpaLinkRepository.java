package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.repository.ListCrudRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

import java.util.List;

// Mark this class annotated as repository or define in configuration

public interface JpaLinkRepository extends ListCrudRepository<Link, Long> {
    // TODO: writer queries to get concrete objects

    //List<GithubLink> findGithubLinks();
    //List<StackOverflowLink> findStackOverflowLinks();

    void deleteByUrlAndChatId(String url, long chatId);
}
