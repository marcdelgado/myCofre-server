package myCofre.server.controller.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivateRequest(

        @Schema(description = "Email address", example = "john.smith@domain.test")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Schema(description = "Activation token", example = "123456")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 6, message = "Activation token")
        String activationToken
)

{

}
