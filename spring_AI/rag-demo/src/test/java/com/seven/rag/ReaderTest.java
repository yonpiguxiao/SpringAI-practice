package com.seven.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import java.util.List;

@SpringBootTest
public class ReaderTest {

    @Test
    void testJsonReader(@Value("classpath:/file/web.json")Resource resource) {
        JsonReader reader = new JsonReader(resource);
        List<Document> documentList = reader.get();
        System.out.printf("document size: %d \n", documentList.size());
        for (Document d : documentList) {
            System.out.println(d.getText());
        }
    }

    @Test
    void testJsonReader2(@Value("classpath:/file/webArray.json")Resource resource) {
        JsonReader reader = new JsonReader(resource);
        List<Document> documentList = reader.get();
        System.out.printf("document size: %d \n", documentList.size());
        for (Document d : documentList) {
            System.out.println(d.getText());
        }
    }


    @Test
    void testTextReader(@Value("classpath:/file/text.txt") Resource resource){
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        System.out.printf("documents: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void testJSOUPReader(@Value("classpath:/file/my-page.html") Resource resource){
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                .selector("article p") // 指定从中提取⽂本的元素 (默认为"body")
                .includeLinkUrls(true) // Include link URLs in metadata
                .metadataTags(List.of("author", "date")) // Extract author and date meta tags
                .additionalMetadata("source", "my-page.html") // Add custom metadata
                .build();
        JsoupDocumentReader reader = new JsoupDocumentReader(resource, config);
        List<Document> documents = reader.get();
        System.out.printf("documents: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }
}
