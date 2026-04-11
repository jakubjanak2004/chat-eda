package app.service;

import app.mapper.ChatUserMapper;
import app.repository.ChatUserRepository;
import app.utils.Elasticsearch;
import dto.response.ChatUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import utils.TextNormalize;

@Service
@Validated
@RequiredArgsConstructor
public class ChatUserService {
    private final ChatUserRepository chatUserRepository;
    private final ChatUserMapper chatUserMapper;

    public Page<ChatUserDTO> getUsersNotUsername(String query, String username, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return chatUserRepository.findByUsernameNot(username, pageable).map(chatUserMapper::toChatUserDTO);
        }
        String normalizedQuery = TextNormalize.normalize(query);
        String pattern = Elasticsearch.toContentWildcardPattern(normalizedQuery);
        return chatUserRepository.findAllByNameNormWildcardNotUsername(pattern, username, pageable)
                .map(chatUserMapper::toChatUserDTO);
    }

    public void createUser(@Valid ChatUserDTO chatUserDTO) {
        chatUserRepository.save(chatUserMapper.toEntity(chatUserDTO));
    }
}
