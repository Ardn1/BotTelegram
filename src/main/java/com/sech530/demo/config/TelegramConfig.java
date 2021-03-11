package com.sech530.demo.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class TelegramConfig {
  @Value(value = "${telegram.token}")
  private String token;
  @Value(value = "${telegram.botname}")
  private String botName;
}
