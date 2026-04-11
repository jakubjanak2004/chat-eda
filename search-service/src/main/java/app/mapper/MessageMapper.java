package app.mapper;

import app.entity.Message;
import dto.response.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MessageMapper {
    @Mapping(target = "responseToSender", ignore = true)
    @Mapping(target = "sender", ignore = true)
    MessageDTO toDTO(Message message);

    @Mapping(source = "responseToSender.username", target = "responseToSenderUsername")
    @Mapping(source = "sender.username", target = "senderUsername")
    Message toEntity(MessageDTO messageDTO);
}
