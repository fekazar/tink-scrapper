package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcScrapperRepository {
    @Autowired
    @Qualifier("scrapperJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ChatRowMapper chatRowMapper;

    @Autowired
    private LinksRowMapper linksRowMapper;

    public List<Chat> getAllChats() {
        return jdbcTemplate.query("select * from chats", chatRowMapper);
    }

    public List<Link> getAllLinks() {
        return jdbcTemplate.query("select * from links", linksRowMapper);
    }

    public List<Link> getLinksForChat(long chatId) {
        return jdbcTemplate.query(
                "select * from links where chat_id = :chatId",
                Map.of("chatId", chatId),
                linksRowMapper
        );
    }

    public boolean hasChat(long chatId) {
        return jdbcTemplate.queryForRowSet(
                "select id from chats where id = :id",
                Map.of("id", chatId)
        ).next();
    }

    public void addChat(long chatId) {
        jdbcTemplate.update(
                "insert into chats (id) values (:chatId)",
                Map.of("chatId", chatId));
    }

    public void addLink(String url, long chatId) {
        jdbcTemplate.update(
                "insert into links (url, chat_id) values (:url, :chatId)",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }

    public void addTypedLink(Link linkRecord, String type) {
        jdbcTemplate.update(
                "insert into links (url, chat_id, last_update, host_type) values (:url, :chatId, :lastUpdate, :hostType)",
                Map.of("url", linkRecord.getUrl(),
                        "chatId", linkRecord.getChatId(),
                        "lastUpdate", linkRecord.getLastUpdate(),
                        "hostType", type)
        );
    }

    public long getLinkId(String url) {
        return jdbcTemplate.queryForObject("select id from links where url = :url",
                Map.of("url", url),
                Long.class);
    }

    public void deleteChat(long chatId) {
        jdbcTemplate.update(
                "delete from chats where id = :id",
                Map.of("id", chatId)
        );
    }

    public void deleteLink(String url, long chatId) {
        jdbcTemplate.update(
                "delete from links where url = :url and chat_id = :chatId",
                Map.of("url", url,
                        "chatId", chatId)
        );
    }

    public void updateLink(long id, Link newRecord) {
        jdbcTemplate.update(
                "update links set url = :url, chat_id = :chatId, last_update = :lastUpdate",
                Map.of("url", newRecord.getUrl(),
                        "chatId", newRecord.getChatId(),
                        "lastUpdate", newRecord.getLastUpdate())
        );
    }

    public void setLastUpdate(String url, OffsetDateTime date) {
        jdbcTemplate.update(
                "update links set last_update = :date where url = :url",
                Map.of("date", date, "url", url)
        );
    }
}
