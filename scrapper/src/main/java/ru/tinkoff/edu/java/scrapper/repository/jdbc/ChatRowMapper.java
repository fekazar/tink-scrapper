package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;

public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chat(rs.getInt("id"));
    }
}
