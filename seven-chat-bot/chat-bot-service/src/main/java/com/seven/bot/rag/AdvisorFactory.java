package com.seven.bot.rag;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
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
                1. 若上下⽂信息中包含与⽤⼾问题直接相关的内容，即使表述⽅式略有不同，也应通过语义理解判断其相关性，并据此进⾏回复。
                2. 在上下⽂中确实⽆法找到与问题相关的任何信息时，请告知⽤⼾联系专业课程顾问。
                3. 回复时直接给出答案或解决⽅案，避免使⽤“根据您提供的信息”、“根据上下⽂”等冗余表达。
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
}
