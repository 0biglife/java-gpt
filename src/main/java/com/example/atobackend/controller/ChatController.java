package com.example.atobackend.controller;

import com.example.atobackend.dto.ChatRequest;
import com.example.atobackend.dto.ChatResponse;
import com.example.atobackend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/sendMessage")
    public ChatResponse chatWithGPT(@RequestBody ChatRequest request) {
        try {
            return chatService.chatGPT(request.getMessage());
        } catch (Exception e) {
            // Exception or Logging
            return new ChatResponse("Error: Unable to process your request.");
        }
    }
}
