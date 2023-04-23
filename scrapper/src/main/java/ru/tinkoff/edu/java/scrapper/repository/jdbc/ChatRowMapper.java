package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chat(rs.getInt("id"));
    }
}
