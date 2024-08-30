package com.ust.spring_ai_llama_chat.controller;

import com.ust.spring_ai_llama_chat.service.LlamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @Autowired
    private LlamaService service;

    @GetMapping("/generate")
    public String generate(@RequestParam(value = "promptMessage") String promptMessage){
        return service.generateResult(promptMessage);
    }
}
