package com.seven.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class WriterTest {
    @Test
    void testFileWriter(@Value("classpath:/file/rule.txt") Resource resource) {
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> split = splitter.split(documents);

        FileDocumentWriter writer = new FileDocumentWriter("output.txt");
        writer.accept(split);
        System.out.println("文档写入完成");
    }

    private SimpleVectorStore simpleVectorStore;

    @Autowired
    public WriterTest(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @Test
    void testSimpleVectorStore(@Value("classpath:/file/rule.txt") Resource resource) {
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> split = splitter.split(documents);

        simpleVectorStore.add(split);
        System.out.println("文档写入完成");
    }
}
