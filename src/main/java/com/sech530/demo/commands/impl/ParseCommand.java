package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import com.sech530.demo.parser.impl.InvestingCom;
import com.sech530.demo.parser.impl.TradingView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Component("ParseCommand")
@RequiredArgsConstructor
public class ParseCommand implements ServiceCommand {
    private final InvestingCom investingCom;
    private final TradingView tradingView;
    private static List<String> jopaList;
    static {
        jopaList = new ArrayList<String>();
      //  jopaList.add("643086785");
    }

    @Override
    public String answer(@NonNull Message message) {
        String messageText = message.getText();
        Integer chatId = message.getFrom().getId();
        System.out.println("chatId: " + chatId);
        if (jopaList.contains(chatId) || true) {
            return "Jopa";
        } else {
            investingCom.processMessage(chatId, messageText);
            tradingView.processMessage(chatId, messageText);
            return "";
        }
    }
}
