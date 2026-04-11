package app.mapper;

import app.entity.Chat;
import dto.response.ChatDTO;
import dto.response.ChatUserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import utils.TextNormalize;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ChatUserMapper.class, MessageMapper.class}
)
public interface ChatMapper {

    @Mapping(target = "chatUsers", ignore = true)
    @Mapping(target = "lastMessage", ignore = true)
    ChatDTO toDTO(Chat chat);

    @BeanMapping(ignoreUnmappedSourceProperties = "lastMessage")
    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = "normalizeChatName")
    Chat toEntity(ChatDTO chatDTO);

    @Named("normalizeChatName")
    default String normalizeChatName(String name) {
        return TextNormalize.normalize(name);
    }
}
