package com.sech530.demo.telegram;

import com.google.common.base.Strings;
import com.sech530.demo.commands.CommandController;
import com.sech530.demo.config.TelegramConfig;

import java.util.List;
import javax.annotation.PostConstruct;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BaseTelegramBot extends TelegramLongPollingCommandBot {
    private final TelegramConfig config;
    private final CommandController commandController;

    public BaseTelegramBot(
            @NonNull TelegramConfig config,
            @NonNull CommandController commandController) {
        super();
        this.config = config;
        this.commandController = commandController;
    }

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onRegister() {
        log.info("Telegram bot has been started");
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::processUpdate);
    }

    private void processUpdate(@NonNull Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String answer = commandController.getPreparedMessage(message);
        sendAnswer(chatId, answer);
    }

    /**
     * Отправка ответа
     *
     * @param chatId id чата
     * @param text   текст ответа
     */
    public void sendAnswer(
            @NonNull Long chatId,
            @NonNull String text) {
        if (Strings.isNullOrEmpty(text)) {
            return;
        }
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram Bot API, используя userName
        }
    }
}
