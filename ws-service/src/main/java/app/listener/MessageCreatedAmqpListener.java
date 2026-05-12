package app.listener;

import app.service.ChatWsService;
import dto.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import messaging.MessageCreatedAmqp;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@link MessageCreatedEvent} from RabbitMQ fanout so every {@code ws-service} replica
 * receives a copy (sessions are spread across instances). Kafka {@code message-created} is still
 * used by {@code search-service} only.
 */
@Component
@RequiredArgsConstructor
public class MessageCreatedAmqpListener {

    private final ChatWsService chatWsService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(
                            value = MessageCreatedAmqp.FANOUT_EXCHANGE_MESSAGE_CREATED,
                            type = ExchangeTypes.FANOUT,
                            durable = "true"
                    )
            )
    )
    public void onMessageCreated(MessageCreatedEvent messageCreatedEvent) {
        chatWsService.sendMessageToUsers(messageCreatedEvent);
    }
}
