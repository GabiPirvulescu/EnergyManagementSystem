package com.example.websocket_service.controllers;

import com.example.websocket_service.dtos.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        System.out.println("📩 Received Chat Message:");
        System.out.println("   From: " + chatMessage.getSenderId() + " (" + chatMessage.getRole() + ")");
        System.out.println("   To: " + chatMessage.getRecipientId());
        System.out.println("   Content: " + chatMessage.getContent());

        boolean isAdmin = "ADMINISTRATOR".equalsIgnoreCase(chatMessage.getRole())
                || "ADMIN".equalsIgnoreCase(chatMessage.getRole());

        if (isAdmin) {
            // Admin sending to Client: use client's personal topic
            String destination = "/topic/chat/" + chatMessage.getRecipientId();
            System.out.println("📤 Admin -> Client: Sending to " + destination);

            messagingTemplate.convertAndSend(destination, chatMessage);

            System.out.println("✅ Message sent to client!");
        } else {
            // Client sending message - check for auto-response first
            String autoResponse = getAutoResponse(chatMessage.getContent());

            if (autoResponse != null) {
                // Auto-reply to the client
                System.out.println("🤖 Auto-Replying to: " + chatMessage.getSenderId());

                ChatMessage reply = new ChatMessage();
                reply.setSenderId("SYSTEM_BOT");
                reply.setRecipientId(chatMessage.getSenderId());
                reply.setContent(autoResponse);
                reply.setRole("ADMINISTRATOR");

                // Send auto-reply to client's personal topic
                String destination = "/topic/chat/" + chatMessage.getSenderId();
                System.out.println("📤 Bot -> Client: Sending to " + destination);

                messagingTemplate.convertAndSend(destination, reply);

                System.out.println("✅ Auto-reply sent!");
            } else {
                // No auto-response - forward to human admin
                System.out.println("📤 Client -> Admin: Broadcasting to /topic/admin");
                messagingTemplate.convertAndSend("/topic/admin", chatMessage);
                System.out.println("✅ Message broadcast to admins!");
            }
        }
    }

    /**
     * Simple Rule Engine
     * Returns a response string if a rule matches, or null if no match.
     */
    private String getAutoResponse(String message) {
        if (message == null) return null;
        String lowerMsg = message.toLowerCase().trim();

        if (lowerMsg.contains("hello") || lowerMsg.contains("hi")) {
            return "👋 Hello! I am an automated support bot. How can I help you today?";
        }
        if (lowerMsg.contains("help")) {
            return "🆘 I can help with: 'hours', 'contact', 'price', or 'location'. Type any keyword or wait for a human admin!";
        }
        if (lowerMsg.contains("hours") || lowerMsg.contains("time")) {
            return "🕒 Our working hours are Mon-Fri, 9:00 AM to 5:00 PM.";
        }
        if (lowerMsg.contains("contact") || lowerMsg.contains("email")) {
            return "📧 You can reach us at support@energy-system.com";
        }
        if (lowerMsg.contains("price") || lowerMsg.contains("cost")) {
            return "💰 Electricity prices are updated hourly based on the market. Check your dashboard for current rates!";
        }
        if (lowerMsg.contains("location") || lowerMsg.contains("address")) {
            return "📍 Our office is located at 123 Energy Street, Tech City. Visit us during business hours!";
        }

        // No match found
        return null;
    }
}