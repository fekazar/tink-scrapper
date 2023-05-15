package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "chats")
@Getter
@Setter
@ToString
public class Chat {
    @Id
    private long id;

    public Chat() {
    }

    public Chat(long id) {
        this.id = id;
    }
}
