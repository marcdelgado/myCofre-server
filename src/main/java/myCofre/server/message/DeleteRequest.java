package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeleteRequest(

        @Schema(description = "Email address", example = "john.smith@domain.test")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Schema(description = "Activation token", example = "123456")
        @Size(min = 0, max = 6, message = "Activation token")
        String deleteToken
)

{

}
