package app.mapper;

import dto.response.ActiveMembershipDTO;
import app.entity.ActiveMembership;
import dto.request.ActiveMembershipUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ActiveMembershipMapper {
    ActiveMembershipDTO toDTO(ActiveMembership activeMembership);
    @Mapping(target="id", ignore = true)
    @Mapping(target = "chatUser", ignore = true)
    @Mapping(target = "chat", ignore = true)
    void updateFromDTO(ActiveMembershipUpdateDTO activeMembershipUpdateDTO, @MappingTarget ActiveMembership activeMembership);
}
