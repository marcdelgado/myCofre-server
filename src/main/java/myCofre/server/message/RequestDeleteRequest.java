package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestDeleteRequest(

        @Schema(description = "Email address", example = "john.smith@domain.test")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,


        @Schema(description = "Language code", example = "en")
        @NotBlank(message = "Language cannot be blank")
        @Size(min = 2, max = 2, message = "Activation token")
        String language
)

{

}
