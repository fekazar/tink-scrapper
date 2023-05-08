package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "pull_requests")
@Getter
@Setter
@ToString
public class PullRequest {
    @Id
    private Long id;

    @Column(name = "link_id")
    private Long linkId;

    // Some fields are transient for now, but may be stored in db in future

    @Transient
    private String url;

    @Transient
    private String title;

    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PullRequest that = (PullRequest) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
