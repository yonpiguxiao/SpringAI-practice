package com.seven.chat.bot.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient ollamaChatClient) {
        this.chatClient = ollamaChatClient;
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }
}
