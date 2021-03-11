package com.sech530.demo.commands.impl;

import com.sech530.demo.commands.ServiceCommand;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Команда "Старт"
 */
@Component("StartCommand")
public class StartCommand implements ServiceCommand {
  @Override
  public String answer(@NonNull Message message) {
    return "Привет!";
  }
}