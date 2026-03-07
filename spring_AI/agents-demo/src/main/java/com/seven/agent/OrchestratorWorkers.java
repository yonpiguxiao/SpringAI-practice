package com.seven.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.Assert;
import java.util.List;

public class OrchestratorWorkers {
    private final ChatClient chatClient;
    private final String orchestratorPrompt;
    private final String workerPrompt;
    public static final String DEFAULT_ORCHESTRATOR_PROMPT = """
                    请分析以下任务, 并将其分解为2-3种不同的写作⻛格或视⻆：
                    任务：{task}
                    要求：
                    - 解释你对该任务的理解
                    - 不同写法应服务于不同受众或使⽤场景
                    - 每个⼦任务需明确类型与具体描述
                    以如下 JSON 格式返回结果：
                    \\{
                        "analysis": "简要说明你的理解, 以及为何这些⻆度有价值",
                        "tasks": [
                            \\{
                            "type": "专业",
                            "description": "⽤正式、技术性强的语⾔突出产品参数和认证标
                            准"
                            \\},
                            \\{
                            "type": "通俗易懂",
                            "description": "⽤⽣活化语⾔讲故事, 强调使⽤体验和情感共鸣"
                            \\}
                        ]
                    \\}
                    """;
    public static final String DEFAULT_WORKER_PROMPT = """
                                请根据以下要求撰写内容：
                                原始任务：{original_task}
                                写作⻛格：{task_type}
                                具体要求：{task_description}
                                输出要求：语⾔流畅⾃然, 适合发布.
                                """;
    /**
     * 表⽰由编排器⽣成的⼀个⼦任务
     *
     *
     * @param type ⻛格类型, 如"专业"、"亲⺠"
     * @param description ⼦任务的具体说明
     */
    public static record Task(String type, String description) {
    }
    /**
     * 编排器的响应：包含对任务的理解和⼦任务列表
     *
     * @param analysis 任务分析说明
     * @param tasks 拆分出的多个⼦任务
     */
    public static record OrchestratorResponse(String analysis, List<Task> tasks) {
    }
    /**
     * 最终输出结果
     *
     * @param analysis 编排器的任务理解
     * @param workerResponses 各⼯作者的输出结果
     */
    public static record FinalResponse(String analysis, List<String>
            workerResponses) {
    }
    /**
     * 使⽤默认提⽰词创建实例
     */
    public OrchestratorWorkers(ChatClient chatClient) {
        this(chatClient, DEFAULT_ORCHESTRATOR_PROMPT, DEFAULT_WORKER_PROMPT);
    }
    /**
     * 使⽤⾃定义提⽰词创建实例
     */
    public OrchestratorWorkers(ChatClient chatClient, String
            orchestratorPrompt, String workerPrompt) {
        Assert.notNull(chatClient, "ChatClient 不能为空");
        Assert.hasText(orchestratorPrompt, "编排器提⽰词不能为空");
        Assert.hasText(workerPrompt, "⼯作者提⽰词不能为空");
        this.chatClient = chatClient;
        this.orchestratorPrompt = orchestratorPrompt;
        this.workerPrompt = workerPrompt;
    }
    /**
     * 执⾏"编排-协作"⼯作流：
     * 1. 编排器分析任务 → 拆分为多视⻆⼦任务
     * 2. 多个⼯作者并⾏处理各⾃⻛格的内容
     * 3. 聚合所有结果
     *
     * @param taskDescription 主任务描述
     * @return 包含分析过程和最终输出的结果
     */
    @SuppressWarnings("null")
    public FinalResponse process(String taskDescription) {
        Assert.hasText(taskDescription, "任务描述不能为空");
// Step 1: 编排器分析任务结构
        OrchestratorResponse orchestratorResponse = this.chatClient.prompt()
                .user(u -> u.text(this.orchestratorPrompt)
                        .param("task", taskDescription))
                .call()
                .entity(OrchestratorResponse.class);
        System.out.println(String.format("\n=== 编排器决策 ===\n分析: %s\n\n拆分任务: %s\n",
        orchestratorResponse.analysis(), orchestratorResponse.tasks()));
// Step 2: 并⾏执⾏每个⼦任务
        List<String> workerResponses =
                orchestratorResponse.tasks().stream().map(task -> this.chatClient.prompt()
                        .user(u -> u.text(this.workerPrompt)
                                .param("original_task", taskDescription)
                                .param("task_type", task.type())
                                .param("task_description", task.description()))
                        .call()
                        .content()).toList();
        System.out.println("\n=== ⼯作者输出 ===\n" + workerResponses);
        return new FinalResponse(orchestratorResponse.analysis(),
                workerResponses);
    }
}

