
package myCofre.server.controller.vault;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import myCofre.server.controller.ApiErrorResponse;
import myCofre.server.domain.Vault;
import myCofre.server.service.VaultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(path = "/myCofre-api/vault", produces = MediaType.APPLICATION_JSON_VALUE)
public class VaultController {

  private final AuthenticationManager authenticationManager;
  private final VaultService vaultService;

  public VaultController(AuthenticationManager authenticationManager, VaultService vaultService) {
    this.authenticationManager = authenticationManager;
    this.vaultService = vaultService;
  }

  @Operation(summary = "Read vault, first time or for sync")
  @ApiResponse(responseCode = "200")
  @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @GetMapping("/read")
  public ResponseEntity<VaultRequest> read(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    Vault vault = vaultService.findByUsername(username);
    if (vault == null) {
      return ResponseEntity.notFound().build();
    }
    VaultRequest vaultRequest = new VaultRequest(new String (vault.getVaultContent(), StandardCharsets.UTF_8), vault.getLastUpdateTimestamp());
    return ResponseEntity.ok(vaultRequest);
  }

  @Operation(summary = "Write vault, first time or for sync")
  @ApiResponse(responseCode = "202")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PutMapping("/write")
  public ResponseEntity<Void> write(@AuthenticationPrincipal UserDetails userDetails, @Valid  @RequestBody VaultRequest vaultRequest) {
    String username = userDetails.getUsername();
    Vault updatedVault = vaultService.write(username, vaultRequest);
    if (updatedVault == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

}