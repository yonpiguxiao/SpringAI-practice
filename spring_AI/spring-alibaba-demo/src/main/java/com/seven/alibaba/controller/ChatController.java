package com.seven.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.seven.alibaba.tools.DateTimeTools;
import com.seven.alibaba.tools.WeatherTools;
import lombok.AllArgsConstructor;
import org.apache.el.util.ReflectionUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private ChatClient chatClient;

    public ChatController(DashScopeChatModel dashScopeChatModel) {
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultTools(new DateTimeTools())
                .build();
    }

    @Autowired
    public ChatController(DashScopeChatModel dashScopeChatModel, ToolCallback weatherTool) {
        chatClient = ChatClient.builder(dashScopeChatModel)
//                .defaultTools(new DateTimeTools())
                .defaultToolCallbacks(weatherTool)
                .build();
    }

    @RequestMapping("/call")
    public String call(String message) {
        return chatClient.prompt()
                .user(message)
                .tools(new DateTimeTools())
                .toolContext(Map.of("chatId", "233"))
                .call()
                .content();
    }

    @RequestMapping("/call2")
    public String call2(String message) {
        Method method = ReflectionUtils.findMethod(WeatherTools.class, "getCurrentWeatherByCityName", String.class);
        ToolCallback toolCallback = MethodToolCallback
                .builder()
                .toolDefinition(ToolDefinitions
                        .builder(method)
                        .description("根据给定的城市名,返回该城市的天气")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();
        return chatClient.prompt()
                .user(message)
                .toolCallbacks(toolCallback)
                .call()
                .content();
    }
}
