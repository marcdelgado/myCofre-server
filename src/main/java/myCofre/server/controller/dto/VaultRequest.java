package myCofre.server.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record VaultRequest(
        @Schema(description = "content")
        byte[] content) {

        public VaultRequest(byte[] content){
                this.content = content;
        }

}
