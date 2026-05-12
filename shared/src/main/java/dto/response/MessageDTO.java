package dto.response;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDTO(
//        @NotNull UUID id,
        UUID responseToId,
        ChatUserDTO responseToSender,
        String responseToContent,
        @NotNull List<String> usernamesList,
        @NotNull UUID chatId,
        @NotNull ChatUserDTO sender,
        @NotNull Instant created,
        @NotNull String content
) {
}