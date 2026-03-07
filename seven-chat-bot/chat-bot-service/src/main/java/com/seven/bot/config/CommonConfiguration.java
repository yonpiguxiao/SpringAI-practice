package com.seven.bot.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.seven.bot.rag.AdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient dashScopeChatClient(DashScopeChatModel chatModel, ChatMemory chatMemory, VectorStore vectorStore){
        return ChatClient.builder(chatModel)
                .defaultSystem("你是⼀名专业的企业培训课程咨询助⼿, 代表【⽐特就业课】为客⼾提供课程咨询服务.\n" +
                        "你的职责是准确、礼貌、⾼效地解答客⼾关于⽐特就业课培训课程的各类问题")
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(AdvisorFactory.createQuestionAnswerAdvisor(vectorStore))
                .build();
    }
}
