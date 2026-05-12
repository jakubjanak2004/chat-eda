package app.service;

import app.observability.WebSocketMetrics;
import dto.event.MessageCreatedEvent;
import dto.response.MessageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Validated
public class ChatWsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWsService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserSessionRegistry userSessionRegistry;
    private final WebSocketMetrics webSocketMetrics;

    public void sendMessageToUsers(@Valid MessageCreatedEvent e) {
        MessageDTO messageDTO = e.messageDTO();

        messageDTO.usernamesList()
                .stream()
                .filter(userSessionRegistry::hasUsername)
                .forEach(username -> {
                    // Standard Spring user destination (works across SockJS/raw WS sessions).
                    simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", messageDTO);

                    // Backward-compatible legacy destination by explicit session suffix.
                    // todo ideally move to the first way of sending the messageDTO defined above so that we do not have to use this sessionId bound method
//                    userSessionRegistry.getSessionSet(username).forEach(
//                            sessionId -> simpMessagingTemplate.convertAndSend("/queue/messages-user" + sessionId, messageDTO)
//                    );
                });

        // record message creation time
        Instant created = messageDTO.created();
        long ms = Duration.between(created, Instant.now()).toMillis();
        if (ms >= 0) {
            webSocketMetrics.messageDeliveryLag().record(Duration.ofMillis(ms));
        }
    }
}
