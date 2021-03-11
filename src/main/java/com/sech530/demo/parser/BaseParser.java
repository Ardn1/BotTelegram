package com.sech530.demo.parser;

@FunctionalInterface
public interface BaseParser {
    void processMessage(Integer chatId, String pair);
}
