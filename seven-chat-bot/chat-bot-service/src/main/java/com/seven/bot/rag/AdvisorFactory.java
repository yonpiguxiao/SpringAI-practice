package com.seven.bot.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.model.RerankModel;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

public class AdvisorFactory {
    public static QuestionAnswerAdvisor createQuestionAnswerAdvisor(VectorStore vectorStore) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        【⽤⼾问题信息】
                        <query>
                        【上下⽂信息】
                        <question_answer_context>
                        回答请遵循以下规则：
                        1. 如果答案在上下⽂中, 你就直接回复, 在必要时, 可以给出参考资料
                        2. 如果用户问题与课程有关, 且答案不在上下⽂中, 请帮忙创建⼯单, 并让⽤⼾等待专业课程顾问回答.
                        3. 如果⽤⼾要求"⼈⼯客服","⼈⼯顾问'来回答, 请帮忙创建⼯单, 并让⽤⼾等待专业课程顾问回答.
                        4. 回复中避免使⽤"根据您提供的信息..."或者"根据上下⽂..."之类的语句
                        确保系统具备基本的语义匹配能⼒，不能仅依赖字⾯完全匹配来判断信息相关性请参考以下对话⽰例的⻛格和⽅式回应客⼾:
                        ---
                        【客⼾】如果来不及听直播的话, 可以回放吗?
                        【助⼿】当然可以！如果您⽆法按时参加直播课程，完全不⽤担⼼。所有直播课程都会全程录制并⽣成回放视频，您可以在【⽐特课堂】学习平台上随时查看，⽀持反复观看、倍速播放等功能。⽆论因何原因错过直播，都不会影响您的学习进度.
                        """)
                .build();
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(promptTemplate)
                .build();
        return advisor;
    }

    public static Advisor createRerankAdvisor(VectorStore vectorStore, RerankModel rerankModel) {
        RetrievalRerankAdvisor advisor = new RetrievalRerankAdvisor(vectorStore, rerankModel,
                SearchRequest.builder().topK(100).build());
        return advisor;
    }
}
