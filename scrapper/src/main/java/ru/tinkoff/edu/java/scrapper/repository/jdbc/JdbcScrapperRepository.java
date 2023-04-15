package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRecord;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.ScrapperRepository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcScrapperRepository implements ScrapperRepository {
    @Autowired
    @Qualifier("scrapperJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ChatRowMapper chatRowMapper;

    @Autowired
    private LinksRowMapper linksRowMapper;


    @Override
    public List<ChatRecord> getAllChats() {
        return jdbcTemplate.query("select * from chats", chatRowMapper);
    }

    @Override
    public List<LinkRecord> getAllLinks() {
        return jdbcTemplate.query("select * from links", linksRowMapper);
    }

    @Override
    public List<LinkRecord> getLinksForChat(int chatId) {
        return jdbcTemplate.query(
                "select * from links where chat_id = :chatId",
                Map.of("chatId", chatId),
                linksRowMapper
        );
    }

    @Override
    public boolean hasChat(int chatId) {
        return jdbcTemplate.queryForRowSet(
                "select id from chats where id = :id",
                Map.of("id", chatId)
        ).next();
    }

    @Override
    public void addChat(int chatId) {
        jdbcTemplate.update(
                "insert into chats (id) values (:chatId)",
                Map.of("chatId", chatId));
    }

    @Override
    public void addLink(String url, int chatId) {
        jdbcTemplate.update(
                "insert into links (url, chat_id) values (:url, chatId)",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }

    @Override
    public void deleteChat(int chatId) {
        jdbcTemplate.update(
                "delete from chats where id = :id",
                Map.of("id", chatId)
        );
    }

    @Override
    public void deleteLink(String url, int chatId) {
        jdbcTemplate.update(
                "delete from links where url = :url and chat_id = :chatId",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }
}
