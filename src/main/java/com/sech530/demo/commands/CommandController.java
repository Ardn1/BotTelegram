package com.sech530.demo.commands;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandController {
    private final ServiceCommand parseCommand;

    private static final Map<String, ServiceCommand> commandHashMap = new HashMap<>();

    public CommandController(
            @NonNull @Qualifier("ParseCommand") ServiceCommand parseCommand,
            @NonNull @Qualifier("StartCommand") ServiceCommand startCommand,
            @NonNull @Qualifier("AllCommand") ServiceCommand allCommand,
            @NonNull @Qualifier("HelpCommand") ServiceCommand helpCommand,
            @NonNull @Qualifier("CostCommand") ServiceCommand costCommand) {
        this.parseCommand = parseCommand;
        commandHashMap.put("/start", startCommand);
        commandHashMap.put("/all", allCommand);
        commandHashMap.put("/help", helpCommand);
        commandHashMap.put("/cost", costCommand);
        commandHashMap.put("/c", costCommand);
        commandHashMap.put("c", costCommand);
    }

    public String getPreparedMessage(@NonNull Message message) {
        String messageText = message.getText();
        String messageCommand = messageText.split(" ")[0];
        if (commandHashMap.containsKey(messageCommand)) {
            return commandHashMap.get(messageCommand).answer(message);
        }
        // Иначе сообщение без команды (то есть что то типо "btc 5min")
        return parseCommand.answer(message);
    }
}
