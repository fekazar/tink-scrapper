package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JpaLinkServiceTest extends JpaIntegrationEnvironment {
    @Test
    void addRemoveLinkTest() {
        jpaChatService.add(1);

        var link = new StackoverflowLink();
        link.setUrl("https://stackoverflow.com/questions/1111/hahahaha");
        link.setChatId(1);

        // link reference should be updated, but service doesnt return any
        jpaLinkRepository.save(link);

        var chats = jpaChatService.getAll();
        var links = jpaLinkService.getAll();

        assertNotNull(chats);
        assertNotNull(links);

        assertEquals(1, chats.size());
        assertEquals(1, links.size());

        jpaLinkService.delete(link.getId());

        assertEquals(0, jpaLinkService.getAll().size());
    }
}
