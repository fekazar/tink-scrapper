package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRecord;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.ScrapperRepository;

import java.sql.Struct;
import java.time.Duration;
import java.time.OffsetDateTime;
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
    public List<LinkRecord> getLinksForChat(long chatId) {
        return jdbcTemplate.query(
                "select * from links where chat_id = :chatId",
                Map.of("chatId", chatId),
                linksRowMapper
        );
    }

    @Override
    public boolean hasChat(long chatId) {
        return jdbcTemplate.queryForRowSet(
                "select id from chats where id = :id",
                Map.of("id", chatId)
        ).next();
    }

    @Override
    public void addChat(long chatId) {
        jdbcTemplate.update(
                "insert into chats (id) values (:chatId)",
                Map.of("chatId", chatId));
    }

    @Override
    public void addLink(String url, long chatId) {
        jdbcTemplate.update(
                "insert into links (url, chat_id) values (:url, :chatId)",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }

    public void addTypedLink(String url, long chatId, String type) {
        jdbcTemplate.update(
                "insert into links (url, chat_id, host_type) values (:url, :chatId, :hostType)",
                Map.of("url", url,
                        "chatId", chatId,
                        "hostType", type)
        );
    }

    @Override
    public void deleteChat(long chatId) {
        jdbcTemplate.update(
                "delete from chats where id = :id",
                Map.of("id", chatId)
        );
    }

    @Override
    public void deleteLink(String url, long chatId) {
        jdbcTemplate.update(
                "delete from links where url = :url and chat_id = :chatId",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }

    @Override
    public void updateLink(long id, LinkRecord newRecord) {
        jdbcTemplate.update(
                "update links set url = :url, chat_id = :chatId, last_update = :lastUpdate",
                Map.of("url", newRecord.url(),
                        "chatId", newRecord.chatId(),
                        "lastUpdate", newRecord.lastUpdate())
        );
    }

    @Override
    public void setLastUpdate(String url, OffsetDateTime date) {
        jdbcTemplate.update(
                "update links set last_update = :date where url = :url",
                Map.of("date", date, "url", url)
        );
    }
}
