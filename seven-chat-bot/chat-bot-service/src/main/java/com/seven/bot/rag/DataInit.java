package com.seven.bot.rag;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class DataInit {
    @Autowired
    private DocumentLoader documentLoader;

    @Autowired
    private KeywordEnricher enricher;

    @Autowired
    private VectorStore vectorStore;

    @Value("${data.is-load}")
    private boolean isLoad;

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @PostConstruct
    public void init() {
        List<Document> documents = documentLoader.loadMarkdown();
        log.info("文档加载完成,文档数:{}", documents.size());
        CustomTokenTextSplitter splitter = new CustomTokenTextSplitter();
        List<Document> splitDocuments = splitter.apply(documents);
        log.info("文档分割完成,文档数:{}", documents.size());
//        List<Document> enrichDocuments = enricher.enrich(documents);
//        log.info("文档关键词生成完成,文档数:{}", documents.size());
//        vectorStore.add(enrichDocuments);
        processDocument(splitDocuments, 10);
        log.info("文档存储完成");
        log.info("知识库搭建完成");
    }

    private void processDocument(List<Document> documents, int batchSize) {
        if(!isLoad) {
            log.info("知识库无需初始化");
            return;
        }
        log.info("开始处理文档, 文档个数:{}, 每批:{}个", documents.size(), batchSize);
        int count = (int)Math.ceil((double)documents.size() / batchSize);
        List<List<Document>> batches = IntStream.range(0, count)
                .mapToObj(i -> new ArrayList<>(documents.subList(i * batchSize,
                        Math.min((i + 1) * batchSize, documents.size()))))
                .collect(Collectors.toList());
        log.info("共分为{}批次,并发添加元数据", batches.size());
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for(List<Document> part : batches) {
            executorService.submit(() -> {
                try {
                    //每个线程创建⾃⼰的enricher实例, 每个Document只被⼀个线程处理-> 安全
                    List<Document> enrich = enricher.enrich(part);
                    vectorStore.add(enrich);
                    log.info("批次写⼊成功, ⽂档数量: {}", enrich.size());
                } catch (Exception e) {
                    log.error("批次写⼊失败: ", e);
                } finally {
                    countDownLatch.countDown();//不论成功失败都减⼀
                }
            });
        }
        try {
            countDownLatch.await(10, TimeUnit.MINUTES);
            log.info("所有批次处理完成");
        } catch (InterruptedException e) {
            log.error("批次处理失败");
            throw new RuntimeException(e);
        }
    }
}
