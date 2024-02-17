
package com.example.atobackend.dto;

public class ChatResponse {
    private String response;

    public ChatResponse(String response) {
        this.response = response;
    }

    // Getter
    public String getResponse() {
        return response;
    }
}