package com.seven.rag;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class VectorStoreTest {
//    @Autowired
//    private DashScopeEmbeddingModel embeddingModel;
//
//    private SimpleVectorStore simpleVectorStore;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SimpleVectorStore simpleVectorStore(DashScopeEmbeddingModel embeddingModel) {
            return SimpleVectorStore.builder(embeddingModel).build();
        }
    }

    @Autowired
    private SimpleVectorStore simpleVectorStore;

    @Autowired
    private ChatModel chatModel;

    @BeforeEach
    void setUp() {
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
        simpleVectorStore.add(Arrays.asList(doc,doc2, doc3, doc4));
        System.out.println("向量数据库初始化完成");
    }

    @Test
    void save() {

    }

    @Test
    void search() {
//        List<Document> documents = simpleVectorStore.similaritySearch("机器学习");
//        System.out.println(documents);
        SearchRequest request = SearchRequest.builder()
                .query("机器学习")
                .topK(5)
                .similarityThreshold(0.4)
                .build();
        List<Document> documents = simpleVectorStore.similaritySearch(request);
        System.out.println(documents);
    }

    @Test
    void testRag() {
        PromptTemplate customPromptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        <query>
                        下⾯是上下⽂信息:
                        ---------------------
                        <question_answer_context>
                        ---------------------
                        根据给定的上下⽂信息, 回答问题, 并遵循以下规则:
                        1. 如果答案不在上下⽂中, 你就说不知道
                        2. 回复中避免使⽤"根据您提供的信息..."或者"根据上下⽂..."之类的语句
                        """).build();
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String message="奥运会什么时候举⾏";
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(simpleVectorStore)
                .promptTemplate(customPromptTemplate)
                .searchRequest(SearchRequest.builder()
                        .query(message)
                        .similarityThreshold(0.4)
                        .topK(5)
                        .build())
                .build();
        String content =chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(simpleVectorStore))
                .user(message).call().content();
        System.out.println(content);
    }
}
