package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;

import java.util.List;

public interface JpaLinkRepository extends ListCrudRepository<Link, Long> {
    // TODO: writer queries to get concrete objects

    @Query(value = "select link from links link where TYPE(link) = StackoverflowLink")
    List<StackoverflowLink> findGithubLinks();

    List<Link> findLinksByChatId(long chatId);

    void deleteByUrlAndChatId(String url, long chatId);
}
