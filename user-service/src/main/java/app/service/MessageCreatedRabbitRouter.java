package app.service;

import dto.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import messaging.MessageCreatedAmqp;
import messaging.WsUserPresenceKeys;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Publishes {@link MessageCreatedEvent} to Rabbit only for {@code ws-service} instances that
 * currently host at least one recipient (per Redis keys written on STOMP CONNECT).
 */
@Service
@RequiredArgsConstructor
public class MessageCreatedRabbitRouter {

    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public void publishToOnlineInstances(MessageCreatedEvent messageCreatedEvent) {
        List<String> usernames = messageCreatedEvent.messageDTO().usernamesList();
        if (usernames == null || usernames.isEmpty()) {
            return;
        }
        List<String> distinct = usernames.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList();
        if (distinct.isEmpty()) {
            return;
        }
        List<String> keys = distinct.stream().map(WsUserPresenceKeys::userToInstance).toList();
        List<String> instanceIds = stringRedisTemplate.opsForValue().multiGet(keys);
        if (instanceIds == null) {
            return;
        }
        Set<String> targets = new LinkedHashSet<>();
        for (int i = 0; i < distinct.size(); i++) {
            String id = i < instanceIds.size() ? instanceIds.get(i) : null;
            if (id != null && !id.isBlank()) {
                targets.add(id);
            }
        }
        for (String routingKey : targets) {
            rabbitTemplate.convertAndSend(MessageCreatedAmqp.DIRECT_EXCHANGE_WS, routingKey, messageCreatedEvent);
        }
    }
}
