package com.seven.agent;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AgentsTest {

    @Test
    void testChainWorkflow(@Autowired ChatModel chatModel) {
        String report = """
                    Q3 季度业务总结：
                    本季度客⼾满意度提升⾄92分.
                    收⼊同⽐增⻓45%.
                    主要市场的市场份额达到23%.
                    客⼾流失率从8%下降⾄5%.
                    新客⼾获取成本为每名⽤⼾43元.
                    产品使⽤率达到78%.
                    员⼯满意度评分为87分.
                    运营利润率提升⾄34%.
                    """;
        new ChainWorkflow(ChatClient.builder(chatModel).build()).chain(report);
    }
}
