package com.seven.bot.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class DocumentLoader {
    @Autowired
    private ResourceLoader resourceLoader;

    public Resource[] getResource(String location) throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        return patternResolver.getResources(location);
    }

    public List<Document> loadMarkdown() {
        List<Document> allDoc = new ArrayList<>();
        try {
            Resource[] resources = getResource("classpath:/file/*.md");
            MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                    .withHorizontalRuleCreateDocument(true)
                    .withIncludeCodeBlock(false)
                    .withIncludeBlockquote(false)
                    .build();
            for (Resource resource: resources) {
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDoc.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("文档加载失败, e:", e);
        }
        log.info("文档加载成功");
        return allDoc;
    }
}
