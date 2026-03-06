package com.seven.ollama.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/ollama")
@RestController
public class OllamaController {
    @Autowired
    private OllamaChatModel chatModel;

    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        return chatModel.stream(message);
    }
}
