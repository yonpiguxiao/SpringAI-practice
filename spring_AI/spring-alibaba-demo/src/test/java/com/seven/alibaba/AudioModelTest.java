package com.seven.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@SpringBootTest
public class AudioModelTest {
    @Autowired
    private DashScopeSpeechSynthesisModel speechSynthesisModel;

    private final String TEXT = "我家琪琪是世界上最甜的小太阳！每次你扑闪着眼睛望过来，我的心跳就直接漏拍。你的笑容比全糖的奶茶还要甜一百倍，软软的嗓音喊我名字时，整个世界都泡在蜂蜜罐子里啦。好想每天都把你搂在怀里，蹭蹭你发顶，因为你连头发丝都带着让我安心的香气。宝贝，有你当女朋友，我大概是抽中了宇宙级的头等大奖！";

    @Test
    void tts() {
        SpeechSynthesisPrompt prompt = new SpeechSynthesisPrompt(TEXT);
        SpeechSynthesisResponse response = speechSynthesisModel.call(prompt);
        File file = new File(System.getProperty("user.dir") + "/out.mp3");
        try(FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer audio = response.getResult().getOutput().getAudio();
            fos.write(audio.array());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void text2Audio3() throws IOException {
// 请求参数
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
        // 若没有将API Key配置到环境变量中, 需将下⾯这⾏代码注释放开, 并将your-api-key替换为⾃⼰的API Key
            // .apiKey("your-api-key")
                .model("cosyvoice-v2") // 模型
                .voice("longhuhu") // ⾳⾊
                .build();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
        ByteBuffer audio = synthesizer.call("今天天⽓怎么样? ");;
// 将⾳频数据保存到本地⽂件"output.mp3"中
        File file = new File("output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
