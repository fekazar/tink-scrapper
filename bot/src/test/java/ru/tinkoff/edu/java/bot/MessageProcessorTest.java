package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.tg.MessageProcessor;
import ru.tinkoff.edu.java.bot.tg.command.CommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.ListCommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.TrackCommandProcessor;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class MessageProcessorTest {
    @Test
    void check_empty_list_msg_processor_Test() {
        final var text = "/list";

        var msgEntity = new MessageEntity(MessageEntity.Type.bot_command, 0, text.length());

        var msg = mock(Message.class);
        when(msg.text()).thenReturn(text);
        when(msg.entities()).thenReturn(new MessageEntity[]{msgEntity});

        Map<String, CommandProcessor> map = new TreeMap<>();

        // For now ListCommandProcessor always return an empty list.
        // Later it's repository will be mocked.

        map.put("list", new ListCommandProcessor());
        var msgProcessor = new MessageProcessor(map);

        assertEquals(ListCommandProcessor.EMPTY_LIST, msgProcessor.process(msg));
    }

    @Test
    void check_no_multi_commands_Test() {
        var entities = new MessageEntity[] {new MessageEntity(MessageEntity.Type.bot_command, 0, 1),
            new MessageEntity(MessageEntity.Type.bot_command, 0, 1),
            new MessageEntity(MessageEntity.Type.bot_command, 0, 1)};

        var msg = mock(Message.class);
        when(msg.entities()).thenReturn(entities);
        when(msg.text()).thenReturn("some text, doesn't matter in this case");

        // Map is not required in this test
        var msgProcessor = new MessageProcessor(null);
        assertEquals(MessageProcessor.MULTIPLE_COMMANDS, msgProcessor.process(msg));
    }

    @Test
    void check_if_offset_not_zero_Test() {
        final var text = "fjkdaslfj ksdlafj ksdlajfk slad /track fjdkaslfjk";

        var msgEntity = new MessageEntity(MessageEntity.Type.bot_command,
                text.indexOf("/track"),
                6);

        var msg = mock(Message.class);
        when(msg.text()).thenReturn(text);
        when(msg.entities()).thenReturn(new MessageEntity[]{msgEntity});

        Map<String, CommandProcessor> map = new TreeMap<>();

        map.put("track", new TrackCommandProcessor());
        var msgProcessor = new MessageProcessor(map);

        assertEquals(TrackCommandProcessor.BAD_URL, msgProcessor.process(msg));
    }

    @Test
    void no_command_Test() {
        var msg = mock(Message.class);

        // Map is not required in this test
        var msgProcessor = new MessageProcessor(null);
        assertEquals(MessageProcessor.NO_COMMAND, msgProcessor.process(msg));
    }

    @Test
    void unsupported_command_Test() {
        final var text = "/test";
        var entities = new MessageEntity[]{ new MessageEntity(MessageEntity.Type.bot_command, 0, text.length())};

        var msg = mock(Message.class);
        when(msg.entities()).thenReturn(entities);
        when(msg.text()).thenReturn(text);

        // Map should be empty for no command to be found
        var msgProcessor = new MessageProcessor(new TreeMap<>());
        assertEquals(MessageProcessor.UNSUPPORTED_COMMAND, msgProcessor.process(msg));
    }
}
