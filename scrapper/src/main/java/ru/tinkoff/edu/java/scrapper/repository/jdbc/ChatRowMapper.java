package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.ChatRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatRowMapper implements RowMapper<ChatRecord> {
    @Override
    public ChatRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatRecord(rs.getInt("id"));
    }
}
