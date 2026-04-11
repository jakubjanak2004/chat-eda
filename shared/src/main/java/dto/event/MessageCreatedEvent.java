package dto.event;

import dto.response.MessageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MessageCreatedEvent(@NotNull @Valid MessageDTO messageDTO) {
}
