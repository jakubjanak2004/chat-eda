package app.service;

import dto.event.ActiveMembershipCreatedEvent;
import dto.event.ChatCreatedEvent;
import dto.event.MessageCreatedEvent;
import dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSubscriber.class);
    private final ChatUserService chatUserService;
    private final MessageService messageService;
    private final ChatService chatService;

    @KafkaListener(topics = "user-created", groupId = "search-service")
    public void listen(UserCreatedEvent userCreatedEvent) {
        LOGGER.info("Received userCreatedEvent: {}", userCreatedEvent);
        chatUserService.createUser(userCreatedEvent.chatUserDTO());
    }

    @KafkaListener(topics = "message-created", groupId = "search-service")
    public void listen(MessageCreatedEvent messageCreatedEvent) {
        LOGGER.info("Received messageCreatedEvent: {}", messageCreatedEvent);
        messageService.createMessage(messageCreatedEvent.messageDTO());
    }

    @KafkaListener(topics = "chat-created", groupId = "search-service")
    public void listen(ChatCreatedEvent chatCreatedEvent) {
        LOGGER.info("Received chatCreatedEvent: {}", chatCreatedEvent);
        chatService.createChat(chatCreatedEvent.chatDTO());
    }

    @KafkaListener(topics = "active-membership-created", groupId = "search-service")
    public void listen(ActiveMembershipCreatedEvent activeMembershipCreatedEvent) {
        LOGGER.info("Received activeMembershipCreatedEvent: {}", activeMembershipCreatedEvent);
        chatService.createActiveMembership(activeMembershipCreatedEvent.chatId(), activeMembershipCreatedEvent.username());
    }
}
