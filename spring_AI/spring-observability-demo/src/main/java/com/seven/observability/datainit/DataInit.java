package com.seven.observability.datainit;


import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInit {
    @Autowired
    private VectorStore vectorStore;
//    @PostConstruct
    public void init() {
        Document doc = Document.builder()
                .text("2025年夏季奥运会将于巴黎举⾏, 预计吸引全球数百万观众")
                .build();
        Document doc2 = Document.builder()
                .text("对⽐学习框架下多语⾔BERT模型的语义表⽰分析")
                .build();
        Document doc3 = Document.builder()
                .text("暮⾊中的⽼槐树在⻛中摇曳, 枯枝划破绯红的晚霞")
                .build();
        Document doc4 = Document.builder()
                .text("基于Transformer的预训练模型在机器翻译中的迁移学习研究")
                .build();
        vectorStore.add(Arrays.asList(doc,doc2, doc3, doc4));
        System.out.println("向量数据库初始化完成");
    }
}
