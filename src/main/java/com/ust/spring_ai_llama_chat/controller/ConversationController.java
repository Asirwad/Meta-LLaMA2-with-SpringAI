package com.ust.spring_ai_llama_chat.controller;

import com.ust.spring_ai_llama_chat.model.ChatResponse;
import com.ust.spring_ai_llama_chat.model.QueryRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@RestController
public class ConversationController {
    private final ChatClient chatClient;

    public ConversationController(ChatClient.Builder chatClientBuilder) {
        InMemoryChatMemory memory = new InMemoryChatMemory();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(memory),
                        new MessageChatMemoryAdvisor(memory)
                )
                .build();
    }

    @PostMapping("/chatWithMemory")
    public ChatResponse chatMemory(@RequestBody QueryRequest request){
        if(request.getConversationId() == null || request.getConversationId().isEmpty()){
            request.setConversationId(UUID.randomUUID().toString());
        }
        String content = this.chatClient.prompt()
                .user(request.getQuery())
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, request.getConversationId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                ).call().content();

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setConversationId(request.getConversationId());
        chatResponse.setResponseContent(content);
        return chatResponse;

    }
}
