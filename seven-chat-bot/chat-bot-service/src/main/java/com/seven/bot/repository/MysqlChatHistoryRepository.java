package com.seven.bot.repository;

import com.seven.bot.entity.ChatInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MysqlChatHistoryRepository extends ChatHistoryRepository {

    @Insert("INSERT INTO chat_sessions (chat_id, title) " +
            "VALUES (#{chatId}, #{title})")
    int insert(@Param("chatId") String chatId, @Param("title") String title);

    @Update("UPDATE chat_sessions SET title = #{title}" +
            "WHERE chat_id = #{chatId}")
    int update(@Param("chatId") String chatId, @Param("title") String title);

    @Select("SELECT COUNT(1) FROM chat_sessions WHERE chat_id = #{chatId}")
    int countByChatId(@Param("chatId") String chatId);

    @Override
    default void save(String chatId, String title) {
        int count = countByChatId(chatId);
        if (count > 0) {
            update(chatId, title);
        } else {
            insert(chatId, title);
        }
    }

    @Delete("DELETE FROM chat_sessions WHERE chat_id = #{chatId}")
    @Override
    void clearByChatId(String chatId);

    @Select("SELECT chat_id, title FROM chat_sessions ORDER BY updated_time")
    @Override
    List<ChatInfo> getChats();
}