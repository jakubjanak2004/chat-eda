package app.mapper;

import dto.response.InvitationDTO;
import dto.response.UserInvitationsDTO;
import app.entity.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses={ChatUserMapper.class}
)
public interface InvitationMapper {
    @Mapping(source = "invitation.chat.name", target = "chatName")
    @Mapping(source = "chat.chatMemberships", target = "chatUsers")
    UserInvitationsDTO toUserInvitationsDTO(Invitation invitation);

    InvitationDTO toInvitationDTO(Invitation invitation);
}
