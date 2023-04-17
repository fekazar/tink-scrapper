package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.GithubLinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class LinksRowMapper implements RowMapper<LinkRecord> {

    @Override
    public LinkRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        LinkRecord res = switch (rs.getString("host_type")) {
            case "github" -> new GithubLinkRecord();
            default -> new LinkRecord();
        };

        res.setId(rs.getInt("id"));
        res.setUrl(rs.getString("url"));
        res.setChatId(rs.getInt("chat_id"));

        OffsetDateTime offsetDateTime = rs.getTimestamp("last_update").toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
        res.setLastUpdate(offsetDateTime);

        return res;
    }
}
