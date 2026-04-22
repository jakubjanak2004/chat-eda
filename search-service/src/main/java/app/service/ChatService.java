package app.service;

import app.mapper.ChatMapper;
import app.repository.ChatRepository;
import app.utils.Elasticsearch;
import dto.response.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import utils.TextNormalize;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public void createChat(ChatDTO chatDTO) {
        chatRepository.save(chatMapper.toEntity(chatDTO));
    }

    @PreAuthorize("@chatUserSecurity.hasUsername(#username, authentication)")
    public Page<ChatDTO> getChatsForUsername(String query, String username, Pageable pageable) {
        String normalized = TextNormalize.normalize(query);
        if (normalized.isEmpty()) {
            return chatRepository.findChatsForMember(username, pageable).map(chatMapper::toDTO);
        }
        String pattern = Elasticsearch.toContentWildcardPattern(normalized);
        return chatRepository.findChatsForMemberAndNameNormalizedWildcard(pattern, username, pageable)
                .map(chatMapper::toDTO);
    }

    public void createActiveMembership(UUID uuid, String username) {

    }
}
