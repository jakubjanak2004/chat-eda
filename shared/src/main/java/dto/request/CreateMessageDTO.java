package dto.request;

import config.props.ValidationConstraints;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateMessageDTO(
        @Size(min = 1, max = ValidationConstraints.MESSAGE_CONTENT_MAX)
        String content,
        UUID replyToId
) {
}
