package app.service.ws;

import dto.event.MessageCreatedEvent;
import dto.response.MessageDTO;
import app.entity.ChatMembership;
import app.entity.Message;
import app.mapper.MessageMapper;
import app.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class ChatWsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWsService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserSessionRegistry userSessionRegistry;

    public void messageCreated(@Valid MessageCreatedEvent e) {
        Message message = messageRepository.findById(e.messageDTO().id()).orElseThrow();
        MessageDTO messageDTO = messageMapper.toDTO(message);

        LOGGER.info("WS publish messageId={}, chatId={}, memberships={}",
                messageDTO.id(), messageDTO.chatId(), message.getChat().getChatMemberships().size());

        message.getChat().getChatMemberships().stream()
                .map(ChatMembership::getChatUser)
                .forEach(chatUser -> {
                    String username = chatUser.getUsername();
                    LOGGER.info("WS sendToUser username={}", username);

                    // Standard Spring user destination (works across SockJS/raw WS sessions).
                    simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", messageDTO);

                    // Backward-compatible legacy destination by explicit session suffix.
                    // todo ideally move to the first way of sending the messageDTO defined above so that we do not have to use this sessionId bound method
                    userSessionRegistry.getSessionSet(username).forEach(
                            sessionId -> simpMessagingTemplate.convertAndSend("/queue/messages-user" + sessionId, messageDTO)
                    );
                });
    }
}
