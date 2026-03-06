package com.seven.alibaba.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/call")
    public String call(String message) {
        return chatClient.prompt().user(message).call().content();
    }

    @GetMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        return chatClient.prompt().user(message).stream().content();
    }

    record ActorFilms(String actor, List<String> movies) {

    }

    @GetMapping("/entity")
    public String entity(String actor) {
        return this.chatClient.prompt()
                .user(String.format("帮我生成演员%s的作品", actor))
                .call()
                .entity(ActorFilms.class).toString();
    }

    @GetMapping("/word")
    public String word(String message, String word) {
        return chatClient.prompt()
                .system(sp -> sp.param("word", word))
                .user(message)
                .call()
                .content();
    }
}
