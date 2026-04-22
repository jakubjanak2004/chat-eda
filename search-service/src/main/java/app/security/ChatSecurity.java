package app.security;

import app.repository.ActiveMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("chatSecurity")
@RequiredArgsConstructor
public class ChatSecurity {
    private final ActiveMembershipRepository activeMembershipRepository;

    public boolean canManageChatWithId(UUID chatId, Authentication authentication) {
        return activeMembershipRepository.existsByChatIdAndUsername(chatId, authentication.getName());
    }
}
