package com.seven.observability.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import io.micrometer.core.instrument.search.Search;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private ChatModel chatModel;

    public ChatClient chatClient;
    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    public TestController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
    @RequestMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }

    @RequestMapping("/callByClient")
    public String callByClient(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call().content();
    }
    @RequestMapping("/search")
    public String search(String query) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThreshold(0.3)
                .build();
        List<Document> documents = vectorStore.similaritySearch(request);
        System.out.println(documents);
        return documents.stream().map(Document::getText).collect(Collectors.joining(":"));
    }
    @RequestMapping("/embedding")
    public String embedding(@RequestParam(defaultValue = "⽐特就业课") String
                                    text) {
        float[] embedded = embeddingModel.embed(text);
        return "Embedding⻓度为:"+embedded.length;
    }
}