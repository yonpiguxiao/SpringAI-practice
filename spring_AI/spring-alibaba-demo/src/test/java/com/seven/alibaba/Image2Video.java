package com.seven.alibaba;

import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Image2Video {
    @Test
    public void image2Video() throws NoApiKeyException, InputRequiredException {
        String imgUrl =
                "https://n.sinaimg.cn/sinakd20230117ac/214/w1080h2334/20230117/e875-42e7995e77807e060ad58b38c0d6ee7a.jpg";
// 设置parameters参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prompt_extend", true);
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param = VideoSynthesisParam.builder()
                        .model("wan2.2-i2v-plus")
                        .prompt("喝⽔")
                        .imgUrl(imgUrl)
                        .parameters(parameters)
                        .resolution("1080P")
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
    }
}
