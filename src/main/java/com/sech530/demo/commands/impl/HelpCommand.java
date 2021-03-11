package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Команда "Старт"
 */
@Component("HelpCommand")
public class HelpCommand implements ServiceCommand {
    @Override
    public String answer(@NonNull Message message) {
        return "/cost btc - посмотреть цену (сокращенные варианты /c, c)\n" +
                "Теперь доступно 2 сервиса!\n" +
                "\uD83D\uDCE2InvestingCom\uD83D\uDCE2:1m 5m 15m 30m 1h 5h 1d 1w\n" +
                "\uD83D\uDCE2TradingView\uD83D\uDCE2: 1m 5m 15m h 4h 1d 1w\n" +
                "Пример команды:btc 15m (но просто 'btc' = 'btc 1d')\n Скорость замедлена из-за полной эмуляции браузера (этого не избежать)";
    }
}