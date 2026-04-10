package dto.event;

import dto.response.ChatUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserCreatedEvent(@NotNull @Valid ChatUserDTO chatUserDTO) {
}
