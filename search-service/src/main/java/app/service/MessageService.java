package app.service;

import app.mapper.ChatMapper;
import app.mapper.ChatUserMapper;
import app.mapper.MessageMapper;
import app.repository.ChatRepository;
import app.repository.ChatUserRepository;
import app.repository.MessageRepository;
import app.utils.Elasticsearch;
import dto.response.ChatDTO;
import dto.response.ChatUserDTO;
import dto.response.MessageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import utils.TextNormalize;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

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
