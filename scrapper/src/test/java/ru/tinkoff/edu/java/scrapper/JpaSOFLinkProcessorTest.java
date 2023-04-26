package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class JpaSOFLinkProcessorTest extends JpaIntegrationEnvironment {
    @Test
    void updatesTest() {
    }
}
