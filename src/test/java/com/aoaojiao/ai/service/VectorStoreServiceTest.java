package com.aoaojiao.ai.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author DD
 */
@SpringBootTest
public class VectorStoreServiceTest {

    @Resource
    private VectorStore vectorStore;

    @Test
    void testAdd() {
        TextReader textReader = new TextReader(new ClassPathResource("/doc/test.txt"));
        List<Document> documents = textReader.read();
        List<Document> documentList = documents.stream()
                .map(document -> {
                    String text = document.getText();
                    if (StringUtils.hasText(text)) {
                        text.replace("\r", "");
                        String[] lines = text.split("\n");
                        return Arrays.stream(lines).map(Document::new).toList();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(document -> StringUtils.hasText(document.getText()) && !"\r".equals(document.getText()))
                .toList();
        vectorStore.add(documentList);
    }

    @Test
    void testSearch() {
        List<Document> results = vectorStore.similaritySearch("红孩儿");
        System.out.println(results);
    }

}
