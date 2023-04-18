package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.records.AnswerRecord;
import ru.tinkoff.edu.java.scrapper.response.AnswersResponse;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcStackAnswersRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<AnswerRecord> getAnswersFor(long linkId) {
        return jdbcTemplate.query(
                "select * from stackoverflow_answers where link_id = :linkId",
                Map.of("linkId", linkId),
                (rs, rowNum) -> new AnswerRecord(rs.getInt("answer_id"), rs.getInt("link_id")));
    }

    public void addAnswer(long linkId, AnswerRecord ans) {
        jdbcTemplate.update(
                "insert into stackoverflow_answers (answer_id, link_id) values (:answerId, :linkId)",
                Map.of("linkId", linkId, "answerId", ans.getAnswerId())
        );
    }

    public void deleteAnswer(long answerId) {
        jdbcTemplate.update(
                "delete from stackoverflow_answers where answer_id = :answer_id",
                Map.of("answer_id", answerId)
        );
    }
}
