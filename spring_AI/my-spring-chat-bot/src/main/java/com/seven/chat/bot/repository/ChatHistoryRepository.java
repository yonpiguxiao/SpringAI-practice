package com.seven.chat.bot.repository;

import com.seven.chat.bot.entity.ChatInfo;
import java.util.List;

public interface ChatHistoryRepository {
    void save(String chatId, String title);
    void clearByChatId(String chatId);
    List<ChatInfo> getChats();
}
