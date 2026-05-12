package app.listener;

import app.service.ChatWsService;
import dto.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@link MessageCreatedEvent} from this instance's Rabbit queue (direct exchange, routing key = instance id).
 * {@code user-service} publishes only to routing keys present in Redis for online recipients.
 */
@Component
@RequiredArgsConstructor
public class MessageCreatedAmqpListener {

    private final ChatWsService chatWsService;

    @RabbitListener(queues = "#{@wsMessageQueue.name}")
    public void onMessageCreated(MessageCreatedEvent messageCreatedEvent) {
        chatWsService.sendMessageToUsers(messageCreatedEvent);
    }
}
