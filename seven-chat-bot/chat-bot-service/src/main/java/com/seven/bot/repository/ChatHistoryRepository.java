package com.seven.bot.repository;

import com.seven.bot.entity.ChatInfo;

import java.util.List;

public interface ChatHistoryRepository {
    void save(String chatId, String title);
    void clearByChatId(String chatId);
    List<ChatInfo> getChats();
}
