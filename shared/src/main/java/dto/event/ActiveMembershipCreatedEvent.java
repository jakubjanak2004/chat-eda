package dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActiveMembershipCreatedEvent(
        @NotNull UUID id,
        @NotNull UUID chatId,
        @NotBlank String username
) {}
