package app.service;

import dto.event.ActiveMembershipCreatedEvent;
import dto.event.ChatCreatedEvent;
import dto.event.MessageCreatedEvent;
import dto.event.UserCreatedEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messaging.MessageCreatedAmqp;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class KafkaPublisher {
    private final KafkaTemplate<String, UserCreatedEvent> userCreatedEventKafkaTemplate;
    private final KafkaTemplate<String, MessageCreatedEvent> messageCreatedEventKafkaTemplate;
    private final KafkaTemplate<String, ChatCreatedEvent> chatCreatedEventKafkaTemplate;
    private final KafkaTemplate<String, ActiveMembershipCreatedEvent> activeMembershipCreatedKafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    public void publishUserCreatedEvent(@Valid UserCreatedEvent userCreatedEvent) {
        userCreatedEventKafkaTemplate.send("user-created", userCreatedEvent);
    }

    public void publishMessageCreatedEvent(@Valid MessageCreatedEvent messageCreatedEvent) {
        messageCreatedEventKafkaTemplate.send("message-created", messageCreatedEvent);
        // ws-service consumes from Rabbit fanout (every replica); search-service still uses Kafka.
        rabbitTemplate.convertAndSend(MessageCreatedAmqp.FANOUT_EXCHANGE_MESSAGE_CREATED, "", messageCreatedEvent);
    }

    public void publishChatCreatedEvent(@Valid ChatCreatedEvent chatCreatedEvent) {
        chatCreatedEventKafkaTemplate.send("chat-created", chatCreatedEvent);
    }

    public void publishActiveMembershipCreated(@Valid ActiveMembershipCreatedEvent activeMembershipCreated) {
        activeMembershipCreatedKafkaTemplate.send("active-membership-created", activeMembershipCreated);
    }
}
