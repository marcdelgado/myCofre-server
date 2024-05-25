package myCofre.server.controller.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT session token")
        String sessionToken
    ){

}
