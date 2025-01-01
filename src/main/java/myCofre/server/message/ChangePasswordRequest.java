package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @Schema(description = "Old repassword", example = "123456")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 20 characters")
        String oldRepassword,

        @Schema(description = "New repassword", example = "123456")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 20 characters")
        String newRepassword

){

}
