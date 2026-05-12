package app.config;

import app.config.props.WebSocketProperties;
import app.service.UserSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtDecoder jwtDecoder;
    private final UserSessionRegistry userSessionRegistry;
    private final WebSocketProperties webSocketProperties;
    @Value("${app.websocket.executor.inbound.core-pool-size:4}")
    private int inboundCorePoolSize;
    @Value("${app.websocket.executor.inbound.max-pool-size:16}")
    private int inboundMaxPoolSize;
    @Value("${app.websocket.executor.inbound.queue-capacity:1000}")
    private int inboundQueueCapacity;
    @Value("${app.websocket.executor.outbound.core-pool-size:8}")
    private int outboundCorePoolSize;
    @Value("${app.websocket.executor.outbound.max-pool-size:32}")
    private int outboundMaxPoolSize;
    @Value("${app.websocket.executor.outbound.queue-capacity:5000}")
    private int outboundQueueCapacity;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WSChannelInterceptor(jwtDecoder, userSessionRegistry));
        registration.taskExecutor()
                .corePoolSize(inboundCorePoolSize)
                .maxPoolSize(inboundMaxPoolSize)
                .queueCapacity(inboundQueueCapacity);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(outboundCorePoolSize)
                .maxPoolSize(outboundMaxPoolSize)
                .queueCapacity(outboundQueueCapacity);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Raw WebSocket STOMP endpoint
        registry.addEndpoint("/ws-raw")
                        .setAllowedOriginPatterns("*");
//                .setAllowedOriginPatterns(webSocketProperties.allowedOrigins().toArray(String[]::new));

        // SockJS endpoint used by browser/app clients.
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(webSocketProperties.allowedOrigins().toArray(String[]::new))
                .withSockJS();
    }
}

