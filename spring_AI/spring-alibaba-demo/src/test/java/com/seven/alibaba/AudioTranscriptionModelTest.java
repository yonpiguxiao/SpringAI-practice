package com.seven.alibaba;


import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.transcription.Transcription;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionQueryParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionResult;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Arrays;

@SpringBootTest
public class AudioTranscriptionModelTest {
    @Autowired
    private DashScopeAudioTranscriptionModel transcriptionModel;

    private final String DEFAULT_MODEL = "paraformer-v2";

    @Test
    void stt() {
        Resource resource = new DefaultResourceLoader()
                .getResource("https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav");
        AudioTranscriptionResponse response = transcriptionModel.call(
                new AudioTranscriptionPrompt(
                        resource,
                        DashScopeAudioTranscriptionOptions.builder()
                                .withModel(DEFAULT_MODEL)
                                .build()
                )
        );
        System.out.println(response.getResult().getOutput());
    }

    @Test
    void sttDashscope() {
        TranscriptionParam param = TranscriptionParam.builder()
                // 若没有将API Key配置到环境变量中, 需将apiKey替换为⾃⼰的APIKey
                //.apiKey("apikey")
                .model("paraformer-v2")
                // "language_hints"只⽀持paraformer-v2模型
                .parameter("language_hints", new String[]{"zh", "en"})
                .fileUrls(
                        Arrays.asList(
                                "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav",
                                "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_male2.wav"))
                .build();
        try {
            Transcription transcription = new Transcription();
            // 提交转写请求
            TranscriptionResult result = transcription.asyncCall(param);
            System.out.println("RequestId: " + result.getRequestId());
            // 阻塞等待任务完成并获取结果
            result = transcription.wait(TranscriptionQueryParam.FromTranscriptionParam(param, result.getTaskId()));
            // 打印结果
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result.getOutput()));
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    @Test
    void testParaformer() {
        // 创建Recognition实例
        Recognition recognizer = new Recognition();
        // 创建RecognitionParam
        RecognitionParam param = RecognitionParam.builder()
                // 若没有将API Key配置到环境变量中, 需将下⾯这⾏代码注释放开, 并将apiKey替换为⾃⼰的API Key
                // .apiKey("yourApikey")
                .model("paraformer-realtime-v2")
                .format("wav")
                .sampleRate(16000)
                // "language_hints"只⽀持paraformer-realtime-v2模型
                .parameter("language_hints", new String[]{"zh", "en"})
                .build();
        try {
//            ClassPathResource resource = new ClassPathResource("hello_world_male_16k_16bit_mono.wav");
//            System.out.println("识别结果：" + recognizer.call(param, resource.getFile()));
            String path = System.getProperty("user.dir") + "/output.wav";
            String result = recognizer.call(param, new File(path));
            System.out.println("识别结果：" + new GsonBuilder().setPrettyPrinting().create().toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 任务结束后关闭 WebSocket 连接
            recognizer.getDuplexApi().close(1000, "bye");
        }
        System.out.println("[Metric] requestId: "
                        + recognizer.getLastRequestId()
                        + ", first package delay ms: "
                        + recognizer.getFirstPackageDelay()
                        + ", last package delay ms: "
                        + recognizer.getLastPackageDelay());
    }
}
