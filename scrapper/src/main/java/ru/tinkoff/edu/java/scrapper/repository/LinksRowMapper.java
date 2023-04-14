package ru.tinkoff.edu.java.scrapper.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LinksRowMapper implements RowMapper<LinkRecord> {

    @Override
    public LinkRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LinkRecord(
                rs.getInt("id"),
                rs.getString("url"),
                rs.getInt("chat_id"));
    }
}
