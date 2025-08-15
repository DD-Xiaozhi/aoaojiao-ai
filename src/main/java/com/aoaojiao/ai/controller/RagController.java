package com.aoaojiao.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author DD
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ai/rag")
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final MessageChatMemoryAdvisor messageChatMemoryAdvisor;

    @RequestMapping("/send")
    public Flux<String> ragChat(@RequestBody String prompt, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return chatClient.prompt()
                .system("你是人类的好朋友，专注解决人类的问题")
                .advisors(messageChatMemoryAdvisor)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        // 查询相似度 0.8 的前 6 个
                        .promptTemplate(getPromptTemp())
                        .build()
                )
                .user(prompt)
                .stream()
                .content();
    }


    private PromptTemplate getPromptTemp() {
        return PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        <query>
                        
                        Context information is below.

                        ---------------------
                        <question_answer_context>
                        ---------------------

                        Given the context information and no prior knowledge, answer the query.

                        Follow these rules:

                        1. If the answer is not in the context, just say that you don't know.
                        2. Avoid statements like "Based on the context..." or "The provided information...".
                        """)
                .build();
    }
}
