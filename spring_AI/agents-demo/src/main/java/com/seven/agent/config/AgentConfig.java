package com.seven.agent.config;

import com.seven.agent.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.Map;

public class AgentConfig {
//    String report = """
//                    Q3 季度业务总结：
//                    本季度客⼾满意度提升⾄92分.
//                    收⼊同⽐增⻓45%.
//                    主要市场的市场份额达到23%.
//                    客⼾流失率从8%下降⾄5%.
//                    新客⼾获取成本为每名⽤⼾43元.
//                    产品使⽤率达到78%.
//                    员⼯满意度评分为87分.
//                    运营利润率提升⾄34%.
//                    """;
//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//        return args -> {
//            new ChainWorkflow(chatClientBuilder.build()).chain(report);
//        };
//    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//
//        return args -> {
//            List<String> parallelResponse = new
//                    ParallelizationWorkflow(chatClientBuilder.build())
//                    .parallel(
//                            "分析数字化转型对此部⻔的主要影响和应对建议, 每条不超过两句话. ",
//                            List.of(
//                                    "销售部：依赖⼈脉, 缺乏数据⼯具",
//                                    "技术部：系统⽼旧, ⼈⼿紧张",
//                                    "⼈⼒资源部：员⼯不适应, 培训不⾜",
//                                    "财务部：控制成本, 担⼼安全"
//                            ),
//                            4
//                    );
//// 输出结果
//            System.out.println("=== 数字化转型简要分析 ===");
//            List.of("销售部", "技术部", "⼈⼒资源部", "财务部")
//                    .forEach(dept ->
//                            System.out.println("\n " + dept + "：" +
//                                    parallelResponse.get(List.of("销售部", "技术部", "⼈⼒资源部", "财务 部").indexOf(dept)))
//                    );
//        };
//    }
//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//        return args -> {
//// 定义四个简单明了的处理路线 (更短的提⽰词)
//            Map<String, String> simpleRoutes = Map.of(
//                    "财务",
//                    "你是财务助⼿, 请回答账单、扣费、退款等问题. 开头写【财务】. ",
//                    "技术",
//                    "你是技术⽀持, 请回答登录、功能使⽤、系统错误等问题. 开头写【技术】. ",
//                    "账⼾",
//                    "你是账⼾助⼿, 请处理账号找回、密码重置、安全验证等问题. 开头写【账⼾】. ",
//                    "产品",
//                    "你是产品顾问, 请回答功能咨询、如何使⽤、推荐操作等问题. 开头写【产品】. "
//            );
//// 模拟三个⾮常简短的⽤⼾提问 (贴近⽇常对话)
//            List<String> simpleTickets = List.of(
//                    "我忘记密码了, 登不进去账号", // → 应路由到"账⼾"
//                    "上个⽉怎么多扣了50块钱? ", // → 应路由到"财务"
//                    "导出数据的功能在哪? 怎么⽤? " // → 应路由到"产品"
//            );
//            var routerWorkflow = new RoutingWorkflow(chatClientBuilder.build());
//            int i = 1;
//            for (String ticket : simpleTickets) {
//                System.out.println("\n💬 问题 " + i++);
//                System.out.println("------------------------------");
//                System.out.println(ticket);
//                System.out.println("------------------------------");
//                String response = routerWorkflow.route(ticket, simpleRoutes);
//                System.out.println(response);
//            }
//        };
//    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
//        var chatClient = chatClientBuilder.build();
//        return args -> {
//            OrchestratorWorkers.FinalResponse response = new OrchestratorWorkers(chatClient)
//                    .process("为⼀款新型环保可重复使⽤的保温杯撰写产品介绍⽂案");
//// 打印最终结果
//            System.out.println("\n\n最终聚合结果：\n");
//            for (int i = 0; i < response.workerResponses().size(); i++) {
//                String style = response.workerResponses().get(i).startsWith("【专业版】") ? "专业" : "亲⺠";
//                        System.out.println(" [" + style + "⻛格] 输出：\n" +
//                                response.workerResponses().get(i) + "\n");
//            }
//        };
//    }
    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder
                                                       chatClientBuilder) {
        var chatClient = chatClientBuilder.build();
        return args -> {
            EvaluatorOptimizer.RefinedResponse refinedResponse = new EvaluatorOptimizer(chatClient).loop("""
            我正在找⼀份专业相关的实习⼯作, 需要每周去公司3天.
            如何写⼀段话, 向辅导员说明情况, 并申请办理实习⼿续?
            希望表达出我对学业的重视, 同时说明实习对未来就业的重要性.
            """);
            System.out.println("FINAL OUTPUT:\n : " + refinedResponse);
        };
    }
}
