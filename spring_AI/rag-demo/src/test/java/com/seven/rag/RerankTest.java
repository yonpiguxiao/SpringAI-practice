package com.seven.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import org.antlr.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RerankTest {

    private SimpleVectorStore simpleVectorStore;

    @Autowired
    public RerankTest(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @BeforeEach
    void testSimpleVectorStore(@Value("classpath:/file/rule.txt") Resource resource) {
//        Document doc = Document.builder()
//                .text("2025年夏季奥运会将于巴黎举⾏, 预计吸引全球数百万观众")
//                .build();
//        Document doc2 = Document.builder()
//                .text("对⽐学习框架下多语⾔BERT模型的语义表⽰分析")
//                .build();
//        Document doc3 = Document.builder()
//                .text("暮⾊中的⽼槐树在⻛中摇曳, 枯枝划破绯红的晚霞")
//                .build();
//        Document doc4 = Document.builder()
//                .text("基于Transformer的预训练模型在机器翻译中的迁移学习研究")
//                .build();
//        simpleVectorStore.add(Arrays.asList(doc,doc2, doc3, doc4));
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter(200, 50, 5, 1000, true);
        List<Document> split = splitter.split(documents);
        simpleVectorStore.add(split);
        System.out.println("向量数据库初始化完成");
    }

    @Test
    void testRerank(@Autowired DashScopeRerankModel rerankModel,
                    @Autowired DashScopeChatModel chatModel) {
        ChatClient client = ChatClient.builder(chatModel).build();

        RetrievalRerankAdvisor advisor = new RetrievalRerankAdvisor(simpleVectorStore,
                rerankModel, SearchRequest.builder().topK(10).build());

        String ret = client.prompt()
                .user("奥运会什么时候举行")
                .advisors(advisor)
                .call()
                .content();
        System.out.println(ret);
    }
}
