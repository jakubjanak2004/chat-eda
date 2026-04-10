package dto.request;

import config.props.ValidationConstraints;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
        @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH, max = ValidationConstraints.USERNAME_MAX_LENGTH)
        String username,
        @Size(min = ValidationConstraints.PASSWORD_MIN_LENGTH, max = ValidationConstraints.PASSWORD_MAX_LENGTH)
        String password,
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {
}
