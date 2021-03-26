package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import com.sech530.demo.parser.impl.InvestingCom;
import com.sech530.demo.parser.impl.TradingView;
import lombok.Data;
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
    public static int jopaCounter;
    public static void jopaCounterIncrease() {
        jopaCounter++;
    }
    public static void jopaCounterDecrease() {
        jopaCounter--;
        if (jopaCounter < 0)
            jopaCounter = 0;
    }
    static {
        jopaList = new ArrayList<String>();
     //   jopaList.add("643086785");
     //   jopaList.add("180689973");
     //   jopaList.add("1120632547");
    }
    private static final int MAX_JOPA = 2;
    private static final boolean JOPA_MODE = true;
    @Override
    public String answer(@NonNull Message message) {
        String messageText = message.getText();
        Integer chatId = message.getFrom().getId();
        System.out.println("chatId: " + chatId + " " + jopaCounter);
        if (jopaList.contains(chatId) || jopaCounter > MAX_JOPA || JOPA_MODE) {
            return "JopaMode";
        } else {
            jopaCounterIncrease();
            investingCom.processMessage(chatId, messageText);
            tradingView.processMessage(chatId, messageText);
            return "";
        }
    }
}