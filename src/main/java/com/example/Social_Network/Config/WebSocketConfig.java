package com.example.Social_Network.Config;

import com.example.Social_Network.WebSocket.WebSocketTokenFiller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final String frontendCallerHost = "http://localhost:5173";

    private final WebSocketTokenFiller webSocketTokenFiller;
    public WebSocketConfig(WebSocketTokenFiller webSocketTokenFiller) {
        this.webSocketTokenFiller = webSocketTokenFiller;
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketTokenFiller);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
            //registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5173").withSockJS();
        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                .setAllowedOrigins(frontendCallerHost);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
            /*registry.setApplicationDestinationPrefixes("/app");
            registry.enableSimpleBroker("/topic");*/

      registry
              .setApplicationDestinationPrefixes("/app")
              .enableSimpleBroker("/topic")
              .setTaskScheduler(heartBeatScheduler())
              .setHeartbeatValue(new long[] {1000L , 1000L});
    }
    @Bean
    public TaskScheduler heartBeatScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
