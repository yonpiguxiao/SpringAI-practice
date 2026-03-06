package com.seven.ai.controller;


import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/ds")
@RestController
public class DeepSeekController {
    @Autowired
    private OpenAiChatModel chatModel;

    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

    @RequestMapping("/chatByPrompt")
    public String chatByPrompt(String message) {
        Prompt prompt = new Prompt(message);
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

    @RequestMapping("/role")
    public String role(String message) {
        SystemMessage systemMessage = new SystemMessage("你叫小琪，是一个自研的智能AI助手，你擅长JAVA和C++，负责日常答疑，请以友好的态度来回答问题");
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        Prompt prompt = new Prompt(message);
        Flux<ChatResponse> response = chatModel.stream(prompt);
        return response.map(x -> x.getResult().getOutput().getText());
    }

}
