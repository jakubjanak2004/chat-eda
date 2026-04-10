package app.service;

import dto.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSubscriber.class);
    private final ChatUserService chatUserService;

    public KafkaSubscriber(ChatUserService chatUserService) {
        this.chatUserService = chatUserService;
    }

    @KafkaListener(topics = "user-created", groupId = "search-service")
    public void listen(UserCreatedEvent userCreatedEvent) {
        LOGGER.info("Received userCreatedEvent: {}", userCreatedEvent);
        chatUserService.createUser(userCreatedEvent.chatUserDTO());
    }
}
