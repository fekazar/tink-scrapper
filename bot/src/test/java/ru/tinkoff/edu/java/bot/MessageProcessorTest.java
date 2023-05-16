package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.tg.MessageProcessor;
import ru.tinkoff.edu.java.bot.tg.command.CommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.ListCommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.TrackCommandProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class MessageProcessorTest {
    private static Message msg;

    @BeforeAll
    static void setup() {
        var chat = mock(Chat.class);
        when(chat.id()).thenReturn(1l);

        msg = mock(Message.class);
        when(msg.chat()).thenReturn(chat);
    }

    @Test
    void check_empty_list_msg_processor_Test() {
        final var text = "/list";

        var msgEntity = new MessageEntity(MessageEntity.Type.bot_command, 0, text.length());

        when(msg.text()).thenReturn(text);
        when(msg.entities()).thenReturn(new MessageEntity[]{msgEntity});

        Map<String, CommandProcessor> map = new TreeMap<>();

        // For now ListCommandProcessor always return an empty list.
        // Later it's repository will be mocked.

        var scrapperClient = mock(ScrapperClient.class);
        when(scrapperClient.getLinks(1)).thenReturn(new LinkResponse[]{});

        map.put("list", new ListCommandProcessor(scrapperClient));
        var msgProcessor = new MessageProcessor(map, null);

        assertEquals(ListCommandProcessor.EMPTY_LIST, msgProcessor.process(msg));
    }

    @Test
    void check_no_multi_commands_Test() {
        var entities = new MessageEntity[] {new MessageEntity(MessageEntity.Type.bot_command, 0, 1),
            new MessageEntity(MessageEntity.Type.bot_command, 0, 1),
            new MessageEntity(MessageEntity.Type.bot_command, 0, 1)};

        when(msg.entities()).thenReturn(entities);
        when(msg.text()).thenReturn("some text, doesn't matter in this case");

        // Map is not required in this test
        var msgProcessor = new MessageProcessor(null, null);
        assertEquals(MessageProcessor.MULTIPLE_COMMANDS, msgProcessor.process(msg));
    }

    @Test
    void check_if_offset_not_zero_Test() {
        final var text = "fjkdaslfj ksdlafj ksdlajfk slad /track fjdkaslfjk";

        var msgEntity = new MessageEntity(MessageEntity.Type.bot_command,
                text.indexOf("/track"),
                6);

        when(msg.text()).thenReturn(text);
        when(msg.entities()).thenReturn(new MessageEntity[]{msgEntity});

        Map<String, CommandProcessor> map = new TreeMap<>();

        map.put("track", new TrackCommandProcessor());
        var msgProcessor = new MessageProcessor(map);

        assertEquals(TrackCommandProcessor.BAD_URL, msgProcessor.process(msg));
    }

    @Test
    void no_command_Test() {
        when(msg.chat().id()).thenReturn(1l);

        // Map is not required in this test
        var msgProcessor = new MessageProcessor(null, null);
        assertEquals(MessageProcessor.NO_COMMAND, msgProcessor.process(msg));
    }

    @Test
    void unsupported_command_Test() {
        final var text = "/test";
        var entities = new MessageEntity[]{ new MessageEntity(MessageEntity.Type.bot_command, 0, text.length())};

        when(msg.entities()).thenReturn(entities);
        when(msg.text()).thenReturn(text);

        // Map should be empty for no command to be found
        var msgProcessor = new MessageProcessor(new TreeMap<>(), null);
        assertEquals(MessageProcessor.UNSUPPORTED_COMMAND, msgProcessor.process(msg));
    }
}
