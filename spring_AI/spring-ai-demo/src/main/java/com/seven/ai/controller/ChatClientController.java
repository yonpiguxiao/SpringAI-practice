package com.seven.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RequestMapping("/chat")
@RestController
public class ChatClientController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/call")
    public String generation(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    record Recipe(String dish, List<String> ingredients) {}

    @RequestMapping("/entity")
    public String entity(String userInput) {
        Recipe recipe = this.chatClient.prompt()
                .user(String.format("请帮我生成%s的食谱",userInput))
                .call()
                .entity(Recipe.class);
        return recipe.toString();
    }

    @GetMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .stream()
                .content();
    }
}
