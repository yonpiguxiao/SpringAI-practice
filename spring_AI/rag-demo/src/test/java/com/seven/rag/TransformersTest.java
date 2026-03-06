package com.seven.rag;

import com.mysql.cj.protocol.a.result.TextBufferRow;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class TransformersTest {
    @Test
    void testSplitter(@Value("classpath:/file/rule.txt")Resource resource) {
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        System.out.println(documents.size());
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> split = splitter.split(documents);
        System.out.println(split.size());
        split.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }

    @Autowired
    private ChatModel chatModel;

    @Test
    void testEnricher(@Value("classpath:/file/rule.txt")Resource resource) {
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        System.out.println(documents.size());
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> split = splitter.split(documents);
        System.out.println(split.size());
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher
                .builder(chatModel)
                .keywordsTemplate(new PromptTemplate("""
                根据给定的⽂本: {context_str}, ⽣成关键字, 只允许以下关键字
                [会员宗旨, 会员类型, 会员注册, 积分制度, 会员权益, 会员⾏为, 会员服务, 公告, 隐
                私保护]
                只返回关键字, 其他信息不返回
                """))
//                .keywordCount(3)
                .build();
        List<Document> enrich = enricher.apply(split);
        enrich.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }

    @Test
    void testSummaryEnricher(@Value("classpath:/file/rule.txt")Resource resource) {
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> split = splitter.split(documents);
        String customPrompt = "根据给定的⽂本: {context_str}, ⽣成⽂档摘要, 限制在50字以内, 只返回摘要, 其他信息不返回";
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,
                List.of(SummaryMetadataEnricher.SummaryType.CURRENT,
                        SummaryMetadataEnricher.SummaryType.NEXT,
                        SummaryMetadataEnricher.SummaryType.PREVIOUS),
                customPrompt, MetadataMode.NONE);
        List<Document> enrich = summaryMetadataEnricher.apply(split);
        enrich.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }
}
