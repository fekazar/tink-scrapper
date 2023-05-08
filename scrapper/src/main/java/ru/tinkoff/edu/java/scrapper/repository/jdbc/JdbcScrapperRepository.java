package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

@AllArgsConstructor
public class JdbcScrapperRepository {
    public static final String CHAT_ID_KEY = "chatId";
    public static final String ID_KEY = "id";
    public static final String LAST_UPDATE_KEY = "lastUpdate";
    public static final String URL_KEY = "url";
    public static final String HOST_TYPE_KEY = "hostType";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ChatRowMapper chatRowMapper;

    private final LinksRowMapper linksRowMapper;

    public List<Chat> getAllChats() {
        return jdbcTemplate.query("select * from chats", chatRowMapper);
    }

    public List<Link> getAllLinks() {
        return jdbcTemplate.query("select * from links", linksRowMapper);
    }

    public List<Link> getLinksForChat(long chatId) {
        return jdbcTemplate.query(
                "select * from links where chat_id = :" + CHAT_ID_KEY,
                Map.of(CHAT_ID_KEY, chatId),
                linksRowMapper
        );
    }

    public boolean hasChat(long chatId) {
        return jdbcTemplate.queryForRowSet(
                "select id from chats where id = :" + ID_KEY,
                Map.of(ID_KEY, chatId)
        ).next();
    }

    public void addChat(long chatId) {
        jdbcTemplate.update(
                String.format("insert into chats (id) values (:%s)", CHAT_ID_KEY),
                Map.of(CHAT_ID_KEY, chatId));
    }

    public void addLink(String url, long chatId) {
        jdbcTemplate.update(
                String.format("insert into links (url, chat_id) values (:%s, :%s)", URL_KEY, CHAT_ID_KEY),
                Map.of(URL_KEY, url,
                        CHAT_ID_KEY, chatId)
        );
    }

    public void addTypedLink(Link linkRecord, String type) {
        jdbcTemplate.update(
                String.format("insert into links (url, chat_id, last_update, host_type) values (:%s, :%s, :%s, :%s)",
                    URL_KEY, CHAT_ID_KEY, LAST_UPDATE_KEY, HOST_TYPE_KEY),

                Map.of(URL_KEY, linkRecord.getUrl(),
                        CHAT_ID_KEY, linkRecord.getChatId(),
                        LAST_UPDATE_KEY, linkRecord.getLastUpdate(),
                        HOST_TYPE_KEY, type)
        );
    }

    public long getLinkId(String url) {
        return jdbcTemplate.queryForObject(String.format("select id from links where url = :%s", URL_KEY),
                Map.of(URL_KEY, url),
                Long.class);
    }

    public void deleteChat(long chatId) {
        jdbcTemplate.update(
                String.format("delete from chats where id = :%s", ID_KEY),
                Map.of(ID_KEY, chatId)
        );
    }

    public void deleteLink(String url, long chatId) {
        jdbcTemplate.update(
                String.format("delete from "
                    + "links where url = :%s and chat_id = :%s", URL_KEY, CHAT_ID_KEY),
                Map.of(URL_KEY, url,
                        CHAT_ID_KEY, chatId)
        );
    }

    public void updateLink(long id, Link newRecord) {
        jdbcTemplate.update(
                String.format("update links set url = :%s, chat_id = :%s, last_update = :%s",
                    URL_KEY, CHAT_ID_KEY, LAST_UPDATE_KEY),
                Map.of(URL_KEY, newRecord.getUrl(),
                        CHAT_ID_KEY, newRecord.getChatId(),
                        LAST_UPDATE_KEY, newRecord.getLastUpdate())
        );
    }

    public void setLastUpdate(String url, OffsetDateTime date) {
        jdbcTemplate.update(
                String.format("update links set last_update = :%s where url = :%s", LAST_UPDATE_KEY, URL_KEY),
                Map.of(LAST_UPDATE_KEY, date, URL_KEY, url)
        );
    }
}
