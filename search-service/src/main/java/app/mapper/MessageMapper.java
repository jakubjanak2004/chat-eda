package app.mapper;

import app.entity.Message;
import dto.response.ChatUserDTO;
import dto.response.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MessageMapper {
    @Mapping(source = "responseToSenderUsername", target = "responseToSender", qualifiedByName = "usernameOnly")
    @Mapping(source = "senderUsername", target = "sender", qualifiedByName = "usernameOnly")
    @Mapping(target = "usernamesList", ignore = true)
    MessageDTO toDTO(Message message);

    @Named("usernameOnly")
    default ChatUserDTO usernameOnly(String username) {
        if (username == null) return null;
        return new ChatUserDTO(username, "", "", false);
    }

    @Mapping(source = "responseToSender.username", target = "responseToSenderUsername")
    @Mapping(source = "sender.username", target = "senderUsername")
    Message toEntity(MessageDTO messageDTO);
}
