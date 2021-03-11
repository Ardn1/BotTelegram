package com.sech530.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BaseController {
    @GetMapping(value = "/health")
    public Mono<String> getHealth() {
        return Mono.just("Ok!");
    }
}
