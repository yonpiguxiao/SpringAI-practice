package com.seven.alibaba.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/multi")
public class MultiModeController {
    private final ChatClient chatClient;

    public MultiModeController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @RequestMapping("/image")
    public String image(String prompt) throws URISyntaxException, MalformedURLException {
        String url = "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg";
        List<Media> mediaList = List.of(new Media(MimeTypeUtils.IMAGE_JPEG, new URI(url).toURL().toURI()));
        UserMessage userMessage = UserMessage.builder().text(prompt).media(mediaList).build();

        return this.chatClient.prompt(new Prompt(userMessage)).call().content();
    }
}
