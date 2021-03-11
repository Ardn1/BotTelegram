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
public class TradingView implements BaseParser {
    private BaseTelegramBot baseTelegramBot;
    private static Map<String, String> timeConverter;
    //https://ru.tradingview.com/symbols/YFIIUSDT/technicals/
    private static final List<String> xpaths;

    static {
        timeConverter = new HashMap<>();
        timeConverter.put("1m","1 минута");
        timeConverter.put("5m","5 минут");
        timeConverter.put("15m","15 минут");
        timeConverter.put("1h","1 час");
        timeConverter.put("4h","4 часа");
        timeConverter.put("1d","1 день");
        timeConverter.put("1w","1 неделя");

        xpaths = List.of(
                "//*[@id=\"technicals-root\"]/div/div/div[2]/div[1]/span",
                "//*[@id=\"technicals-root\"]/div/div/div[2]/div[2]/span[2]",
                "//*[@id=\"technicals-root\"]/div/div/div[2]/div[3]/span",
                "//*[@id=\"anchor-page-1\"]/div/div[3]/div[1]/div/div/div/div[1]/div[1]/span");
    }

    @Autowired
    public TradingView(@Lazy BaseTelegramBot baseTelegramBot) {
        this.baseTelegramBot = baseTelegramBot;
    }

    private String formatMessage(
            @NonNull String pair,
            String interval,
            String price,
            @NonNull String firstSignal,
            @NonNull String secondSignal,
            @NonNull String thirdSignal) {
        if (interval == null) {
            interval = "24 часа";
        }
        return String.format(
                "\uD83D\uDCE2TradingView\uD83D\uDCE2\n%s/USDT за %s\n%s Осцилляторы:  %s\n%s Статистика:  %s\n%s Скользящие средние:  %s\n",
                pair.toUpperCase(Locale.ROOT),
                interval,
                getEmoji(firstSignal),
                firstSignal,
                getEmoji(secondSignal),
                secondSignal,
                getEmoji(thirdSignal),
                thirdSignal
        );
    }

    @Override
    public void processMessage(Integer chatId, String pair) {
        String clearPair = pair.contains(" ")
                ? pair.substring(0, pair.indexOf(" "))
                : pair;

        String url =
                "https://ru.tradingview.com/symbols/" + clearPair.toUpperCase(Locale.ROOT) + "USDT/technicals/";
        String interval = null;
        String oldInterval = null;
        if (pair.contains(" ")) {
            String tempInterval = pair.substring(pair.indexOf(" ") + 1);
            if (!Strings.isEmpty(tempInterval) && timeConverter.containsKey(tempInterval)) {
                interval = "//div[text()='" + timeConverter.get(tempInterval) + "']";
                oldInterval = timeConverter.get(tempInterval);
            } else {
                baseTelegramBot.sendAnswer(Long.valueOf(chatId), "TradingView не имеет интервал:" + tempInterval);
                return;
            }
        }
        String finalInterval = interval;
        String finalOldInterval = oldInterval;
        new Thread(() -> {
            List<String> result = null;
            try {
                result = RemoteDriver.parseData(url, xpaths, finalInterval);
                System.out.println(result);
                if (result == null) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = formatMessage(clearPair.toUpperCase(Locale.ROOT),
                            finalOldInterval, result.get(3), result.get(0), result.get(1), result.get(2));
            baseTelegramBot.sendAnswer(Long.valueOf(chatId), message);
        }).start();
    }
}