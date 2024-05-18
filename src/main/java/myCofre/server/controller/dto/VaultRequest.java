package myCofre.server.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record VaultRequest(
        @Schema(description = "content")
        String content) {

        public VaultRequest(String content){
                this.content = content;
        }

}
