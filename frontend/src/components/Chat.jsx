import React, { useState, useEffect, useRef } from 'react';
import WebSocketService from '../services/WebSocketService';
import './Chat.css';

const Chat = ({ currentUser, chatPartner }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState("");
    const messagesEndRef = useRef(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    // Send Message Function
    const sendMessage = () => {
        if (inputMessage.trim() && WebSocketService.stompClient?.connected) {
            const message = {
                senderId: currentUser.userId,
                recipientId: chatPartner ? chatPartner.id : "ADMIN",
                content: inputMessage,
                role: currentUser.role
            };

            console.log("📤 Sending message:", message);

            // Send via WebSocket
            WebSocketService.sendChatMessage(message);

            // Add to our own UI
            setMessages(prev => [...prev, { ...message, isMine: true }]);
            setInputMessage("");
        } else {
            console.error("❌ Cannot send message - WebSocket not connected");
        }
    };

    // Listen for incoming messages via custom event
    useEffect(() => {
        const handleChatMessage = (event) => {
            const msg = event.detail;
            console.log("💬 Chat UI received:", msg);
            
            const myRole = currentUser.role;
            const senderRole = msg.role;
            const senderId = msg.senderId;

            const isAdmin = myRole === 'ADMINISTRATOR' || myRole === 'ADMIN';
            const isBotMessage = senderId === 'SYSTEM_BOT';

            let isRelevant = false;

            if (isAdmin) {
                // Admin sees messages from clients they're chatting with
                if (chatPartner && senderId === chatPartner.id) {
                    isRelevant = true;
                }
            } else {
                // Client sees messages from Admin OR Bot
                if (senderRole === 'ADMINISTRATOR' || senderRole === 'ADMIN' || isBotMessage) {
                    isRelevant = true;
                }
            }

            if (isRelevant) {
                console.log("✅ Message is relevant, adding to chat");
                setMessages(prev => [...prev, { 
                    ...msg, 
                    isMine: false,
                    isBot: isBotMessage 
                }]);
            } else {
                console.log("⚠️ Message filtered (not relevant)");
            }
        };

        window.addEventListener('chatMessage', handleChatMessage);

        return () => {
            window.removeEventListener('chatMessage', handleChatMessage);
        };
    }, [currentUser.role, currentUser.userId, chatPartner]);

    return (
        <div className="chat-container">
            <div className="chat-header" onClick={() => setIsOpen(!isOpen)}>
                {isOpen ? `💬 Chat with ${chatPartner ? chatPartner.username : 'Support'}` : "💬 Chat"}
            </div>
            
            {isOpen && (
                <>
                    <div className="chat-messages">
                        {messages.length === 0 && (
                            <div style={{ textAlign: 'center', color: '#999', padding: '20px' }}>
                                No messages yet. Start the conversation! 
                                {!chatPartner && <div style={{ marginTop: '10px', fontSize: '12px' }}>Try typing "help" or "hello" 🤖</div>}
                            </div>
                        )}
                        {messages.map((msg, index) => (
                            <div 
                                key={index} 
                                className={`message-bubble ${msg.isMine ? 'sent' : 'received'}`}
                                style={msg.isBot ? { backgroundColor: '#e3f2fd', borderLeft: '3px solid #2196f3' } : {}}
                            >
                                <div style={{ fontSize: '10px', color: '#666', marginBottom: '2px' }}>
                                    {msg.isMine ? 'You' : msg.isBot ? '🤖 Bot' : (chatPartner?.username || 'Admin')}
                                </div>
                                {msg.content}
                            </div>
                        ))}
                        <div ref={messagesEndRef} />
                    </div>
                    <div className="chat-input">
                        <input 
                            type="text" 
                            value={inputMessage}
                            onChange={(e) => setInputMessage(e.target.value)}
                            placeholder="Type a message..."
                            onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                        />
                        <button onClick={sendMessage}>Send</button>
                    </div>
                </>
            )}
        </div>
    );
};

export default Chat;
