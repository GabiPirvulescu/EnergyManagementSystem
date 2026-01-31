package com.example.websocket_service.dtos;

public class ChatMessage {
    private String senderId;
    private String recipientId;
    private String content;
    private String role;

    public ChatMessage() {}

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}