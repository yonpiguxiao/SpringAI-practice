package com.seven.agent;

import com.seven.agent.entity.RoutingResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.Assert;

import java.util.Map;

public class RoutingWorkflow {
    private final ChatClient chatClient;
    public RoutingWorkflow(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    /**
     * 根据内容分类, 将输⼊路由到专⽤提⽰词进⾏处理.
     * 流程包括：
     * 1. 分析输⼊内容以确定最佳类别
     * 2. 选择该类别的优化提⽰词
     * 3. 使⽤选定提⽰词处理输⼊
     *
     * 优势：
     * - 更好地处理多样化请求
     * - 按场景定制提⽰词提升准确性
     * - 实现专业化响应
     */
    public String route(String input, Map<String, String> routes) {
        Assert.notNull(input, "输⼊⽂本不能为空");
        Assert.notEmpty(routes, "路由映射表不能为空");
// 第⼀步：决定⾛哪个路由
        String routeKey = determineRoute(input, routes.keySet());
// 获取对应的专业提⽰词
        String selectedPrompt = routes.get(routeKey);
        if (selectedPrompt == null) {
            throw new IllegalArgumentException("选中的路由 '" + routeKey + "' 在路由表中未定义");
        }
// 执⾏专业处理
        return chatClient.prompt(selectedPrompt + "\n输⼊内容：\n" +
                input).call().content();
    }
    /**
     * 使⽤ LLM 分析输⼊内容, 从可⽤选项中选择最合适的⽀持团队
     */
    @SuppressWarnings("null")
    private String determineRoute(String input, Iterable<String>
            availableRoutes) {
        System.out.println("\n【路由分析】可⽤路由：" + availableRoutes);
        String selectorPrompt = String.format("""
            请分析以下⽤⼾请求, 并从这些⽀持团队中选择最合适的⼀个：%s
            请先说明你的判断理由, 然后以如下 JSON 格式输出结果：
            {
                "reasoning": "简要解释为何应分配给该团队. 考虑关键词、⽤⼾意图和紧急程度. ",
                "selection": "所选团队名称 (必须是上述列表中的某⼀个) "
            }
            ⽤⼾请求：
            %s
            """, availableRoutes, input);
        RoutingResponse routingResponse = chatClient.prompt(selectorPrompt).call().entity(RoutingResponse.class);
        System.out.println(String.format("路由决策分析：%s\n→ 最终选择：%s",
                routingResponse.reasoning(), routingResponse.selection()));
        return routingResponse.selection();
    }
}