package com.sech530.demo.parser.impl;

import static com.sech530.demo.utils.MessageUtils.getEmoji;

import com.sech530.demo.parser.BaseParser;
import com.sech530.demo.parser.RemoteDriver;
import com.sech530.demo.telegram.BaseTelegramBot;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.NonNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class InvestingCom implements BaseParser {
    //https://ru.investing.com/indices/investing.com-eth-usd-technical
    private BaseTelegramBot baseTelegramBot;
    private static Map<String, String> timeConverter;
    private static final List<String> xpaths;
 /* = List.of(
      "//*[@id=\"techStudiesInnerWrap\"]/div[2]/span[2]",
      "//*[@id=\"techStudiesInnerWrap\"]/div[3]/span[2]",
      "//*[@id=\"last_last\"]");*/

    static {
        timeConverter = new HashMap<>();
        timeConverter.put("1m", "1 мин");
        timeConverter.put("5m", "5 мин");
        timeConverter.put("15m", "15 мин");
        timeConverter.put("30m", "30 мин");
        timeConverter.put("1h", "1 час");
        timeConverter.put("5h", "5 часов");
        timeConverter.put("1d", "1 день");
        timeConverter.put("1w", "1 неделя");

        xpaths = List.of(
                "//*[@id=\"techStudiesInnerWrap\"]/div[2]/span[2]",
                "//*[@id=\"techStudiesInnerWrap\"]/div[3]/span[2]",
                "//*[@id=\"last_last\"]");
    }

    @Autowired
    public InvestingCom(@Lazy BaseTelegramBot baseTelegramBot) {
        this.baseTelegramBot = baseTelegramBot;
    }

    private String formatMessage(
            @NonNull String pair,
            String interval,
            String price,
            @NonNull String medium,
            @NonNull String technical) {
        if (interval == null) {
            interval = "24 часа";
        }
        return String.format(
                "\uD83D\uDCE2InvestingCom\uD83D\uDCE2\n%s/USDT за %s\n%s Скол. средние:  %s\n%s Тех. индикаторы:  %s\n",
                pair.toUpperCase(Locale.ROOT),
                interval,
                getEmoji(medium),
                medium,
                getEmoji(technical),
                technical
        );
    }

    @Override
    public void processMessage(Integer chatId, String pair) {
        String clearPair = pair.contains(" ")
                ? pair.substring(0, pair.indexOf(" "))
                : pair;
        String url = "https://ru.investing.com/indices/investing.com-" + clearPair.toLowerCase() + "-usd-technical";
        String interval = null;
        String oldInterval = null;

        if (pair.contains(" ")) {
            String tempInterval = pair.substring(pair.indexOf(" ") + 1);
            if (!Strings.isEmpty(tempInterval) && timeConverter.containsKey(tempInterval)) {
                interval = "//a[text()='" + timeConverter.get(tempInterval) + "']";
                oldInterval = timeConverter.get(tempInterval);
            } else {
                baseTelegramBot.sendAnswer(Long.valueOf(chatId), "InvestingCom не имеет интервал:" + tempInterval);
                return;
            }
        }

        String finalInterval = interval;
        String finalOldInterval = oldInterval;
        new Thread(() -> {
            List<String> result = null;
            try {
                result = RemoteDriver.parseData(url, xpaths, finalInterval);
                if (result == null) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = formatMessage(clearPair.toUpperCase(Locale.ROOT), finalOldInterval, result.get(2), result.get(0), result.get(1));
            baseTelegramBot.sendAnswer(Long.valueOf(chatId), message);
        }).start();
    }
}
