package com.aoaojiao.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

/**
 * AI 问答管理
 *
 * @author DD
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ai/chat")
public class AIChatController {

    private final ChatClient chatClient;
    private final MessageChatMemoryAdvisor messageChatMemoryAdvisor;

    @PostMapping("/send")
    public Flux<String> chat(@RequestBody String prompt, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return chatClient.prompt()
                .system("你是一个编程助手，专注解决用户的编程问题")
                .advisors(messageChatMemoryAdvisor)
                .user(prompt)
                .stream()
                .content();
    }
}
