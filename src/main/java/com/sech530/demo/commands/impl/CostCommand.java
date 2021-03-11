package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import com.sech530.demo.parser.impl.CostParser;
import com.sech530.demo.parser.impl.InvestingCom;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Команда "Стоимость"
 */
@Component("CostCommand")
@RequiredArgsConstructor
public class CostCommand implements ServiceCommand {
    private final CostParser costParser;

    @Override
    public String answer(@NonNull Message message) {
        String messageText = message.getText().split(" ")[1];
        Integer chatId = message.getFrom().getId();
        costParser.processMessage(chatId, messageText);
        return "";
    }
}
