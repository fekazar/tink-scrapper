package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaStackOverflowLinkProcessor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JpaSOFLinkProcessorTest {
    @Autowired
    JpaStackOverflowLinkProcessor linkProcessor;
    
    @Autowired
    JpaLinkService linkService;
    
    @Autowired
    JpaChatService chatService;

    @Autowired
    JpaLinkRepository linkRepository;
    
    @Test
    void updatesTest() {
        chatService.add(154);
        linkService.add("https://stackoverflow.com/questions/70574880/unable-to-install-express-graphql", 154);

        assertEquals(1, chatService.getAll().size());
        assertEquals(1, linkService.getAll().size());

        StackoverflowLink sofLink = (StackoverflowLink) linkRepository.findAll().get(0);
        assertNotNull(sofLink);
        assertNotNull(sofLink.getAnswers());
        assertEquals(0, sofLink.getAnswers().size());

        List<StackoverflowLink> sofLinks = linkRepository.findGithubLinks();
        assertNotNull(sofLinks);
        assertEquals(1, sofLinks.size());
    }
}
