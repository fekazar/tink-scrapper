package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcPullsRepository {
    @Autowired
    @Qualifier("scrapperJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    // There is a binary string in repository, each bit is indicating whether a pull request is open or closed.
    public String pullsStringForUrl(String url) {
        //pulls_status
        return jdbcTemplate.queryForObject(
                "select (pulls_status) from github_pulls where url = :url",
                Map.of("url", url),
                String.class
        );
    }

    public void updatePulls(String url, String newPulls) {
        jdbcTemplate.update("update github_pulls set pulls_status = :newPulls where url = :url",
                Map.of("newPulls", newPulls,
                        "url", url));
    }

    public void createNewPullString(String url) {
        jdbcTemplate.update("insert into github_pulls (url) values (:url)",
                Map.of("url", url));
    }
}
