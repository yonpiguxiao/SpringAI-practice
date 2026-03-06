package com.seven.chat.bot.entity;

import lombok.Data;

@Data
public class ChatInfo {
    public ChatInfo(String chatId, String title) {
        this.chatId = chatId;
        this.title = title == null ? "新会话" : title.length() > 15 ? title.substring(0, 15) : title;
    }

    private String chatId;
    private String title;
}
