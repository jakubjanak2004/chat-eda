package app.service;

import dto.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSubscriber.class);
    private final ChatWsService chatWsService;

    @KafkaListener(topics = "message-created", groupId = "ws-service")
    public void listen(MessageCreatedEvent messageCreatedEvent) {
        LOGGER.info("Received messageCreatedEvent: {}", messageCreatedEvent);
        chatWsService.sendMessageToUsers(messageCreatedEvent);
    }
}
