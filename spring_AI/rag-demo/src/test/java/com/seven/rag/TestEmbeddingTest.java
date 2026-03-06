package com.seven.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestEmbeddingTest {
    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    @Test
    void textEmbed() {
        float[] e = embeddingModel.embed("I love you");
        System.out.println(e.length);
        System.out.println(Arrays.toString(e));
    }

    @Test
    void batchTextEmbed() {
        List<String> list = List.of("I love you","I love you","I love you","I love you","I love you",
                "I love you","I love you","I love you","I love you","I love you","I love you","I love you","I love you",
                "I love you","I love you","I love you","I love you","I love you","I love you","I love you");
        List<float[]> e = embeddingModel.embed(list);
        System.out.println(e.size());
    }
}
