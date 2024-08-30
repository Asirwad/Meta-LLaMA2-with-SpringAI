package com.ust.spring_ai_llama_chat.controller;

import org.springframework.ai.chat.model.ChatResponse;
import com.ust.spring_ai_llama_chat.model.QueryRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class MainController {
    private final ChatClient chatClient;

    public MainController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/basicChat")
    public String basicChat(@RequestBody QueryRequest request){
        PromptTemplate promptTemplate = new PromptTemplate(request.getQuery());
        Prompt prompt = promptTemplate.create();
        ChatClient.CallPromptResponseSpec res = chatClient.prompt(prompt).call();
        return res.chatResponse().getResults().stream().map(g -> g.getOutput().getContent()).findFirst().orElse("");
    }

    @PostMapping("/chatWithSystemPrompt")
    public String basicChatWithSystemPrompt(@RequestBody QueryRequest request){
        Message userMessage = new UserMessage(request.getQuery());
        Message systemMessage = new UserMessage("You are an expert in Java. Please provide to the point and accurate answers.");
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        ChatClient.CallPromptResponseSpec res = chatClient.prompt(prompt).call();
        return res.chatResponse().getResults().stream().map(g->g.getOutput().getContent()).findFirst().orElse("");
    }

    @PostMapping("/chatWithStreamingResponse")
    public Flux<ChatResponse> chatWithStreamingResponse(@RequestBody QueryRequest request){
        Message userMessage = new UserMessage(request.getQuery());
        Message systemMessage = new UserMessage("You are an expert in Java. Please provide to the point and accurate answers.");
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        Flux<ChatResponse> flux = chatClient.prompt(prompt).stream().chatResponse();

        flux.subscribe(
                chatResponse -> System.out.print(chatResponse.getResult().getOutput().getContent()),
                throwable -> System.err.println("Error occurred: " + throwable.getMessage()),
                () -> System.out.println("Stream completed")
        );

        return flux;
    }



}
