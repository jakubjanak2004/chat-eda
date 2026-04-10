package dto.request;

import enumeration.MembershipType;
import jakarta.validation.constraints.NotNull;

public record ActiveMembershipUpdateDTO(@NotNull MembershipType membershipType) {
}
