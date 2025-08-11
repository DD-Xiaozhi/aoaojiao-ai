package com.aoaojiao.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

/**
 * AI 聊天控制器
 *
 * @author DD
 */
@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class AIChatController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    @PostMapping("/send")
    public Flux<String> chat(@RequestBody String prompt, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return chatClient.prompt()
                .system("你是一个编程助手，专注解决用户的变成问题")
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors()
                .user(prompt)
                .stream()
                .content();
    }
}
