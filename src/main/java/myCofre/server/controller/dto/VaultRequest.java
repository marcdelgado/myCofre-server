package myCofre.server.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record VaultRequest(
        @Schema(description = "name")
        String name,
        @Schema(description = "content")
        byte[] content) {

}
