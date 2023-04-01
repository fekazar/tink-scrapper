package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

// To add more logic to this processor new CommandProcessor beans should be
// added to configuration.

@Slf4j
@Component
public class MessageProcessor {
    // TODO: create preconfigured default answers
    private static final String NO_COMMAND = "There is no command in request.";
    private static final String UNSUPPORTED_COMMAND = "This is an unsupported command.";
    private static final String MULTIPLE_COMMANDS = "Multiple commands are unsupported in one request.";

    @Autowired
    private Map<String, CommandProcessor> map;

    public SendMessage process(Update update) {
        var msg = update.message();

        if (msg != null && msg.entities() != null) {
            long commandsCnt = Arrays.stream(msg.entities())
                    .filter(messageEntity -> messageEntity.type().equals(MessageEntity.Type.bot_command))
                    .count();

            if (commandsCnt > 1)
                return new SendMessage(msg.chat().id(), MULTIPLE_COMMANDS);

            if (commandsCnt == 0)
                return new SendMessage(msg.chat().id(), NO_COMMAND);

            var commandEntity = msg.entities()[0];
            String command = msg.text().substring(commandEntity.offset() + 1, commandEntity.length());

            if (!map.containsKey(command)) {
                return new SendMessage(msg.chat().id(), UNSUPPORTED_COMMAND);
            } else {
                return map.get(command).process(msg);
            }
        } else if (update.editedMessage() != null) {
            return new SendMessage(update.editedMessage().chat().id(), "Requests for edited messages are not supported.");
        }

        // Which chat id to use, when there is another type of update?
        return null;
    }

    @PostConstruct
    void init() {
        log.info("Message processor keyset: " + map.keySet());
    }
}
