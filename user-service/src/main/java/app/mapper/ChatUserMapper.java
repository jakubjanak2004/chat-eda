package app.mapper;

import dto.event.UserCreatedEvent;
import dto.request.ChatUserUpdateDTO;
import dto.response.ChatUserDTO;
import dto.request.SignUpDTO;
import app.entity.ChatMembership;
import app.entity.ChatUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ChatUserMapper {
    ChatUserDTO toChatUserDTO(ChatUser user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "hasProfilePicture", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "nameNormalized", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "chatMemberships", ignore = true)
    void updateFromDto(ChatUserUpdateDTO chatUserUpdateDTO, @MappingTarget ChatUser user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasProfilePicture", ignore = true)
    @Mapping(target = "nameNormalized", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "chatMemberships", ignore = true)
    ChatUser toEntity(SignUpDTO signUpDTO);

    @Mapping(source = "chatUser", target = ".")
    ChatUserDTO fromMembership(ChatMembership membership);

    @Mapping(source = "chatUser", target = "chatUserDTO")
    UserCreatedEvent toUserCreatedEvent(ChatUser chatUser);
}
