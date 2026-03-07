package com.seven.agent;

import org.springframework.ai.chat.client.ChatClient;

/**
 * 实现"提⽰链" (Prompt Chaining) ⼯作流模式：
 * 将复杂任务分解为多个连续的 LLM 调⽤, 每⼀步处理上⼀步的输出.
 *
 * <p>
 * ⽰例流程 (分析季度业绩中的关键指标) ：
 * <ol>
 * <li>提取数值和对应指标</li>
 * <li>统⼀转换为百分⽐格式</li>
 * <li>按数值从⾼到低排序</li>
 * <li>⽣成 Markdown 表格</li>
 * </ol>
 *
 * <p/>
 * 适⽤场景：当任务可清晰拆解为多个⼦步骤时使⽤. 通过分步降低单次推理难度, 提升准确率, 牺
 牲⼀点延迟换取更⾼质量输出.
 *
 * @author 改编⾃ Christian Tzolov (中⽂本地化)
 */
public class ChainWorkflow {
    /**
     * 四个阶段的系统提⽰语 (中⽂) , 定义每⼀步的数据转换规则
     */
    private static final String[] DEFAULT_SYSTEM_PROMPTS = {
            // 第⼀步：提取数值与指标
            """
            请从⽂本中提取所有数字及其对应的业务指标.
            每⾏只写⼀个：'数值: 指标名称'
            ⽰例格式：
            92: 客⼾满意度
            45%: 收⼊增⻓率
            """,
            // 第⼆步：标准化为百分⽐
            """
            将所有数值统⼀转换为百分⽐格式.
            - 若是"点"或"分", 直接视为百分⽐ (如 87分 → 87%)
            - ⾮百分⽐的⼩数也转为百分⽐ (如 0.78 → 78%)
            - ⾦额类、⽤⼾数等⾮⽐例数据保留原值并标注[跳过]
            每⾏保持 '数值%: 指标名称' 或 '数值[跳过]: 指标名称'
            ⽰例：
            92%: 客⼾满意度
            45%: 收⼊增⻓率
            $43[跳过]: 新客获取成本
            """,
            // 第三步：降序排列
                        """
            将所有以百分⽐表⽰的指标按数值从⾼到低排序.
            跳过的条⽬ (带[跳过]) 放在最后, 不参与排序.
            格式保持不变：
            92%: 客⼾满意度
            78%: 产品使⽤率
            $43[跳过]: 新客获取成本
            """,
            // 第四步：⽣成表格
                        """
            将排序后的数据整理成 Markdown 表格, 包含两列：
            | 指标 | 数值 |
            |:---|--:|
            ⽰例⾏：
            | 客⼾满意度 | 92% |
            | 新客获取成本 | $43 [未转换] |
            注意：跳过的项⽬显⽰为原始值 + [未转换]
            """
    };
    private final ChatClient chatClient;
    private final String[] systemPrompts;
    public ChainWorkflow(ChatClient chatClient) {
        this(chatClient, DEFAULT_SYSTEM_PROMPTS);
    }
    public ChainWorkflow(ChatClient chatClient, String[] systemPrompts) {
        this.chatClient = chatClient;
        this.systemPrompts = systemPrompts;
    }
    /**
     * 执⾏提⽰链流程：输⼊ → 提取 → 标准化 → 排序 → 表格化
     *
     * @param userInput ⽤⼾输⼊的原始⽂本
     * @return 最终结构化输出 (Markdown 表格)
     */
    public String chain(String userInput) {
        int step = 0;
        String response = userInput;
        System.out.println("✅ 开始执⾏提⽰链⼯作流\n");
        System.out.println(String.format("STEP %d - 原始输⼊:\n%s", step++,
                response));
        for (String prompt : systemPrompts) {
            String input = String.format("%s\n--- 输⼊内容 ---\n%s", prompt,
                    response);
            response = chatClient.prompt(input).call().content();
            System.out.println(String.format("\nSTEP %d - 处理结果:\n%s",
                    step++, response));
        }
        return response;
    }
}