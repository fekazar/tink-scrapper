package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
@Slf4j
public class LinksRowMapper implements RowMapper<Link> {

    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link res = switch (rs.getString("host_type")) {
            case "github" -> new GithubLink();
            case "stackoverflow" -> new StackoverflowLink();
            default -> new Link();
        };

        res.setId(rs.getInt("id"));
        res.setUrl(rs.getString("url"));
        res.setChatId(rs.getInt("chat_id"));

        OffsetDateTime offsetDateTime = rs.getTimestamp("last_update").toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
        res.setLastUpdate(offsetDateTime);

        return res;
    }
}
