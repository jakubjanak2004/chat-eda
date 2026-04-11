package app.mapper;

import app.entity.ChatUser;
import dto.response.ChatUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import utils.TextNormalize;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ChatUserMapper {
    ChatUserDTO toChatUserDTO(ChatUser user);

    @Mapping(target = "nameNormalized", expression = "java(normalizeFullName(chatUserDTO))")
    @Mapping(target = "authorities", ignore = true)
    ChatUser toEntity(ChatUserDTO chatUserDTO);

    default String normalizeFullName(ChatUserDTO chatUserDTO) {
        return TextNormalize.normalize(String.format("%s %s", chatUserDTO.firstName(), chatUserDTO.lastName()));
    }
}
