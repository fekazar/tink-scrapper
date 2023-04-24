package ru.tinkoff.edu.java.scrapper.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.tinkoff.edu.java.scrapper.repository.pojo.PullRequest;

import java.time.OffsetDateTime;

@ToString
public class GithubRepository {

    @JsonProperty("pushed_at")
    private  OffsetDateTime pushedAt;

    @JsonIgnore
    @Setter
    @Getter
    private PullRequest[] pulls;

    public OffsetDateTime pushedAt() {
        return pushedAt;
    }

    public String getDescription() {
        var sb = new StringBuilder();
        sb.append("Pushed at: ").append(pushedAt);
        return sb.toString();
    }
}
