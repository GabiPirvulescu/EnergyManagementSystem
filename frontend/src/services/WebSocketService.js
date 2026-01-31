import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

const WEBSOCKET_URL = 'http://localhost/ws';

class WebSocketService {
    stompClient = null;
    alertCallback = null;
    chatCallback = null;

    connect(userId, role, onAlertReceived, onChatReceived) {
        if (this.stompClient && this.stompClient.connected) {
            console.log("⚠️ Already connected!");
            return;
        }

        console.log("🔌 Connecting WebSocket...");
        console.log("   User ID:", userId);
        console.log("   Role:", role);

        const socket = new SockJS(WEBSOCKET_URL);
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null;

        this.alertCallback = onAlertReceived;
        this.chatCallback = onChatReceived;

        this.stompClient.connect({}, () => {
            console.log("✅ WebSocket Connected!");

            // 1. Subscribe to ALERTS
            const alertTopic = `/topic/alerts/${userId}`;
            console.log("📡 Subscribing to:", alertTopic);
            this.stompClient.subscribe(alertTopic, (message) => {
                if (message.body && this.alertCallback) {
                    this.alertCallback(JSON.parse(message.body));
                }
            });

            // 2. Subscribe to PERSONAL CHAT (for this specific user)
            const chatTopic = `/topic/chat/${userId}`;
            console.log("📡 Subscribing to:", chatTopic);
            this.stompClient.subscribe(chatTopic, (message) => {
                console.log("💬 Personal chat message received:", message.body);
                if (message.body && this.chatCallback) {
                    this.chatCallback(JSON.parse(message.body));
                }
            });

            // 3. Subscribe to ADMIN TOPIC (only for admins)
            if (role === 'ADMINISTRATOR' || role === 'ADMIN') {
                console.log("📡 Admin: Subscribing to /topic/admin");
                this.stompClient.subscribe('/topic/admin', (message) => {
                    console.log("💬 Admin topic message received:", message.body);
                    if (message.body && this.chatCallback) {
                        this.chatCallback(JSON.parse(message.body));
                    }
                });
            }

            console.log("✅ All subscriptions complete!");

        }, (error) => {
            console.error("❌ WebSocket Connection Error:", error);
        });
    }
    
    sendChatMessage(message) {
        if (this.stompClient && this.stompClient.connected) {
            console.log("📤 Sending chat message:", message);
            this.stompClient.send("/app/chat", {}, JSON.stringify(message));
        } else {
            console.error("❌ Cannot send message - WebSocket not connected!");
        }
    }

    disconnect() {
        if (this.stompClient) {
            try {
                this.stompClient.disconnect(() => {
                    console.log("WebSocket Disconnected");
                });
            } catch (error) {
                console.warn("⚠️ WebSocket closed during connection phase:", error);
            }
            this.stompClient = null;
        }
    }
}

export default new WebSocketService();