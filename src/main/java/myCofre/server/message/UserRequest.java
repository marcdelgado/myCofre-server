package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @Schema(description = "Name", example = "John")
        @Size(min = 2, max = 50, message = "Password must be between 2 and 50 characters")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Schema(description = "Surname", example = "Smith")
        @Size(min = 2, max = 50, message = "Password must be between 2 and 50 characters")
        @NotBlank(message = "Surname cannot be blank")
        String surname,

        @Schema(description = "Email address", example = "john.smith@domain.test")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email
)

{

}
