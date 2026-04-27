package app.mapper;

import dto.event.ChatCreatedEvent;
import dto.response.ChatDTO;
import app.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ChatUserMapper.class, MessageMapper.class}
)
public interface ChatMapper {
    @Mapping(source = "chat.id", target = "id")
    @Mapping(source = "chat.name", target = "name")
    @Mapping(source = "chat.chatMemberships", target = "chatUsers")
    ChatDTO toDTO(Chat chat);

    @Mapping(target = "chatDTO", expression = "java(toDTO(chat))")
    ChatCreatedEvent toChatCreatedEvent(Chat chat);
}
