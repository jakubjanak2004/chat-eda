package dto.event;

import dto.response.ChatDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ChatCreatedEvent(@NotNull @Valid ChatDTO chatDTO) {
}
