package com.example.riskManager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Clients will subscribe to paths starting with "/topic" to get updates
        config.enableSimpleBroker("/topic");
        // Clients will send messages to paths starting with "/app" (if they were sending data to us)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint the frontend dashboard will connect to
        registry.addEndpoint("/ws-risk")
                .setAllowedOriginPatterns("*") // Allow frontend to connect from different ports
                .withSockJS(); // Fallback for older browsers
    }
}