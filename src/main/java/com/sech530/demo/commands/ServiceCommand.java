package com.sech530.demo.commands;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;

@FunctionalInterface
public interface ServiceCommand {
    String answer(@NonNull Message message);
}