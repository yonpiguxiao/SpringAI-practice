package com.seven.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.seven.alibaba.tools.DateTimeTools;
import com.seven.alibaba.tools.WeatherTools;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RequestMapping("/chatByModel")
@RestController
public class ChatModelController {
    private ChatModel chatModel;

    public ChatModelController(DashScopeChatModel dashScopeChatModel) {
        this.chatModel = dashScopeChatModel;
    }

    @RequestMapping("/call")
    public String call(String message) {
        return chatModel.call(message);
    }

    @RequestMapping("/callByTool")
    public String callByTool(String message) {
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    @RequestMapping("/callByTool2")
    public String callByTool2(String message) {
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
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
