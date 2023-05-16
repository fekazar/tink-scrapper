package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.tg.command.CommandProcessor;

// To add more logic to this processor new CommandProcessor beans should be
// added to configuration.

@Slf4j
@Component
public class MessageProcessor {
    // TODO: create preconfigured default answers
    public static final String NO_COMMAND = "There is no command in request.";
    public static final String UNSUPPORTED_COMMAND = "This is an unsupported command.";
    public static final String MULTIPLE_COMMANDS = "Multiple commands are unsupported in one request.";

    private final Map<String, CommandProcessor> map;
    private final Counter messageCounter;

    public MessageProcessor(Map<String, CommandProcessor> map, Counter messagesCounter) {
        this.map = map;
        this.messageCounter = messagesCounter;
    }

    // Message cannot be null. This method should be called only
    // to process a new message and no other type of updates.
    // Returns the message text to be sent to user.
    public String process(@NotNull Message msg) {
        if (messageCounter != null)
            messageCounter.increment();

        if (msg.entities() == null)
            return NO_COMMAND;

        long commandsCnt = Arrays.stream(msg.entities())
                .filter(messageEntity -> messageEntity.type().equals(MessageEntity.Type.bot_command))
                .count();

        if (commandsCnt > 1)
            return MULTIPLE_COMMANDS;

        if (commandsCnt == 0)
            return  NO_COMMAND;

        var commandEntity = msg.entities()[0];
        String command = msg.text().substring(commandEntity.offset() + 1, commandEntity.offset() + commandEntity.length());
        log.info("Command arrived: " + command);

        var res = UNSUPPORTED_COMMAND;
        if (map.containsKey(command))
            res = map.get(command).process(command, msg.text(), msg.chat().id());

        return res;
    }

    @PostConstruct
    void init() {
        log.info("Message processor keyset: " + map.keySet());
    }
}
