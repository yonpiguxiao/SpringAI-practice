package com.seven.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient ChatClientController(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem("你叫小琪，是一个自研的智能AI助手，你擅长JAVA和C++，负责日常答疑，请以友好的态度来回答问题")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
