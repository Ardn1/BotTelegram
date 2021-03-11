package com.sech530.demo.parser.impl;

import com.sech530.demo.parser.BaseParser;
import com.sech530.demo.parser.RemoteDriver;
import com.sech530.demo.telegram.BaseTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CostParser implements BaseParser {
    //https://www.rbc.ru/crypto/currency/btcusd
    private BaseTelegramBot baseTelegramBot;
    private static Map<String, String> timeConverter;
    private static final List<String> cssSelector;

    static {
        cssSelector = List.of(
                "body > div.l-window > div > div.l-window-body.js-filter-lock-block > div.l-col-main > div > div.l-row-border > div > div.l-col-center > div > div:nth-child(1) > div > div.chart__info > div.chart__subtitle.js-chart-value");
    }

    @Autowired
    public CostParser(@Lazy BaseTelegramBot baseTelegramBot) {
        this.baseTelegramBot = baseTelegramBot;
    }

    @Override
    public void processMessage(Integer chatId, String pair) {
        String clearPair = pair.contains(" ")
                ? pair.substring(0, pair.indexOf(" "))
                : pair;
        String url = "https://www.rbc.ru/crypto/currency/" + clearPair.toLowerCase() + "usd";

        String result = "Ошибка драйвера";
        try {
            result = RemoteDriver.parseCostData(url, cssSelector);
            if (result == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        baseTelegramBot.sendAnswer(Long.valueOf(chatId), result);
    }
}
