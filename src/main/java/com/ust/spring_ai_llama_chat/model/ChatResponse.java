package com.ust.spring_ai_llama_chat.model;

public class ChatResponse {
    private String conversationId;
    private String responseContent;

    public String getConversationId() {
        return conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    public String getResponseContent() {
        return responseContent;
    }
    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
