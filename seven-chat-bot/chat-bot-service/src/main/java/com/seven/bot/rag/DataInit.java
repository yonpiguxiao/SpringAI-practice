package com.seven.bot.rag;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataInit {
    @Autowired
    private DocumentLoader documentLoader;

    @Autowired
    private KeywordEnricher enricher;

    @Autowired
    private VectorStore vectorStore;

    @PostConstruct
    public void init() {
        List<Document> documents = documentLoader.loadMarkdown();
        log.info("文档加载完成,文档数:{}", documents.size());
        CustomTokenTextSplitter splitter = new CustomTokenTextSplitter();
        List<Document> splitDocuments = splitter.apply(documents);
        log.info("文档分割完成,文档数:{}", documents.size());
        List<Document> enrichDocuments = enricher.enrich(documents);
        log.info("文档关键词生成完成,文档数:{}", documents.size());
        vectorStore.add(enrichDocuments);
        log.info("文档存储完成");
        log.info("知识库搭建完成");
    }
}
