package myCofre.server.controller.vault;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public record VaultResponse(
        @Schema(description = "Vault content. This is encrypted data")
        String vaultContent,

        @Schema(description = "Last update timestamp (for check sync)")
        Timestamp lastUpdateTimestamp
    ){

}
