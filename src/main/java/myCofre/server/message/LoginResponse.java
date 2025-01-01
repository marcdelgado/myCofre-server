package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT session token")
        String sessionToken
    ){

}
