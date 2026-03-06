package com.seven.alibaba;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageModelTest {
    @Autowired
    DashScopeImageModel imageModel;
    @Test
    void text2Img(){
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("孩⼦在海边玩耍"));
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imageUrl);
    }

    @Test
    void text2Img2(){
        String prompt = "中国⼥孩, 圆脸, 看着镜头, 优雅的⺠族服装, 商业摄影, 室外, 电影级光 照, 半⾝特写, 精致的淡妆, 锐利的边缘. ";
        DashScopeImageOptions imageOptions = DashScopeImageOptions.builder()
                .withModel("wan2.2-t2i-flash")
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(prompt,imageOptions));
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imageUrl);
    }
}
