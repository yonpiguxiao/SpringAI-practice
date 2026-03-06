package com.seven.mcpsse.config;


import com.seven.mcpsse.service.UserService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {
    @Bean
    public ToolCallbackProvider getUserInfo(UserService userService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userService)
                .build();
    }
}
