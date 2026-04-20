package app.mapper;

import app.entity.Chat;
import app.entity.ChatMembership;
import app.entity.ChatUser;
import app.entity.Message;
import dto.event.MessageCreatedEvent;
import dto.request.CreateMessageDTO;
import dto.response.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MessageMapper {
    @Mapping(source = "responseTo.id", target = "responseToId")
    @Mapping(source = "responseTo.chatUser", target = "responseToSender")
    @Mapping(source = "responseTo.content", target = "responseToContent")
    @Mapping(source = "chatUser", target = "sender")
    @Mapping(source = "message.chat.id", target = "chatId")
    @Mapping(source = "chat", target = "usernamesList", qualifiedByName = "toUsernamesList")
    MessageDTO toDTO(Message message);

    @Named("toUsernamesList")
    default List<String> toUsernamesList(Chat chat) {
        return chat.getChatMemberships().stream()
                .map(ChatMembership::getChatUser)
                .map(ChatUser::getUsername)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "responseTo", ignore = true)
    @Mapping(target = "responses", ignore = true)
    Message toEntity(CreateMessageDTO createMessageDTO, Chat chat, ChatUser chatUser, Instant created);

    @Mapping(source = "message", target = "messageDTO")
    MessageCreatedEvent toMessageCreatedEvent(Message message);

    MessageCreatedEvent toMessageCreatedEvent(MessageDTO messageDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createMessageDTO.replyToId", target = "responseToId")
    @Mapping(target = "responseToSender", ignore = true)
    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "chatUser", target = "sender")
    @Mapping(target = "responseToContent", ignore = true)
    @Mapping(target = "usernamesList", ignore = true)
    MessageDTO toDTO(CreateMessageDTO createMessageDTO, Chat chat, ChatUser chatUser, Instant created);
}
