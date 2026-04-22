package app.service;

import app.mapper.MessageMapper;
import app.repository.MessageRepository;
import app.utils.Elasticsearch;
import dto.response.MessageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @PreAuthorize("@chatSecurity.canManageChatWithId(#chatId, authentication)")
    public Page<MessageDTO> getMessagesForChat(String query, UUID chatId, Pageable pageable) {
        if (!StringUtils.hasText(query)) {
            return messageRepository.findByChatId(chatId, pageable)
                    .map(messageMapper::toDTO);
        }
        String wildcardPattern = Elasticsearch.toContentWildcardPattern(query.trim());
        return messageRepository.findAllByChatIdAndContentWildcard(wildcardPattern, chatId, pageable)
                .map(messageMapper::toDTO);
    }

    public void createMessage(@Valid MessageDTO messageDTO) {
        messageRepository.save(messageMapper.toEntity(messageDTO));
    }
}
