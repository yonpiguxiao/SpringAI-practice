package com.seven.rag;
import com.alibaba.cloud.ai.reader.mysql.MySQLDocumentReader;
import com.alibaba.cloud.ai.reader.mysql.MySQLResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class MySQLDocumentReaderTest {
    private MySQLResource mysqlResource;
    private MySQLDocumentReader reader;
    @BeforeEach
    void setUp() {
        List<String> contentColumns = List.of("User", "Host");
        List<String> metadataColumns = List.of("User", "Host");
// Setup test MySQL resource
        String query = "SELECT * FROM user LIMIT 10;";
        mysqlResource = new MySQLResource("localhost", 3306, "mysql", "root",
                "568313", query, contentColumns,
                metadataColumns);
        reader = new MySQLDocumentReader(mysqlResource);
    }
    @Test
    void testGetDocuments() {
        List<Document> documents = reader.get();
        System.out.printf("documents: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }
}