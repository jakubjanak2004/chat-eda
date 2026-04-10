package app.service;

import dto.event.UserCreatedEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class KafkaPublisher {
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void publishUserCreatedEvent(@Valid UserCreatedEvent userCreatedEvent) {
        kafkaTemplate.send("user-created", userCreatedEvent);
    }
}
