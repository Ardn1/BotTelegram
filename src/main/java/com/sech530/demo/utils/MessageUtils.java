package com.sech530.demo.utils;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
    private static final String up = "\uD83D\uDCC8";
    private static final String down = "\uD83D\uDCC9";
    private static final String neutral = "\uD83D\uDCCA";
    private static final String BUY_MSG = "покупать";
    private static final String NEUTRAL_MSG = "нейтрально";

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    public static String getUserName(@NonNull Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public static String getEmoji(String signal) {
        if (containsIgnoreCase(signal, NEUTRAL_MSG)) {
            return neutral;
        }
        return containsIgnoreCase(signal, BUY_MSG)
                ? up
                : down;
    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}
