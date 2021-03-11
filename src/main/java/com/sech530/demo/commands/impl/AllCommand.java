package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import com.sech530.demo.telegram.BaseTelegramBot;
import com.sech530.demo.utils.MessageUtils;

import java.util.List;
import javax.annotation.PostConstruct;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@RequiredArgsConstructor
@Component("AllCommand")
public class AllCommand implements ServiceCommand {
    @Setter
    @Value("${chatIds:#{T(java.util.Set).of()}}")
    private List<Integer> chatIds = List.of();
    private BaseTelegramBot baseTelegramBot;

    @Autowired
    public AllCommand(@Lazy BaseTelegramBot baseTelegramBot) {
        this.baseTelegramBot = baseTelegramBot;
    }

    @PostConstruct
    public void update() {
        log.info("Set chatIds:{}", chatIds);
    }

    private boolean isMessageTextOk(String messageText) {
        int spaceIndex = messageText.indexOf(" ");
        return (spaceIndex != -1) && (spaceIndex < (messageText.length() - 1));
    }

    @Override
    public String answer(@NonNull Message message) {
        String messageText = message.getText();
        if (!isMessageTextOk(messageText)) {
            return "Неверный синтаксис команды /all";
        }
        messageText = MessageUtils.getUserName(message) + "\n" + messageText.substring(messageText.indexOf(" ") + 1);
        for (Integer chatId : chatIds) {
            if (!message.getFrom().getId().equals(chatId)) {
                baseTelegramBot.sendAnswer(Long.valueOf(chatId), "\uD83D\uDCE2 " + messageText);
            }
        }
        return "Успешно отправил сообщение";
    }
}
