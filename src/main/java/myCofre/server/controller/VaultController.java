
package myCofre.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import myCofre.server.controller.dto.*;
import myCofre.server.domain.LoginAttempt;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/vault", produces = MediaType.APPLICATION_JSON_VALUE)
public class VaultController {

  private final AuthenticationManager authenticationManager;
  private final VaultService vaultService;

  public VaultController(AuthenticationManager authenticationManager, VaultService vaultService) {
    this.authenticationManager = authenticationManager;
    this.vaultService = vaultService;
  }

  @Operation(summary = "Read vault")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @GetMapping("/read")
  public ResponseEntity<VaultRequest> getVault(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    Vault vault = vaultService.findByUsername(username);
    if (vault == null) {
      return ResponseEntity.notFound().build();
    }
    VaultRequest vaultDTO = new VaultRequest(new String (vault.getContent(), StandardCharsets.UTF_8));
    return ResponseEntity.ok(vaultDTO);
  }

  @Operation(summary = "Create vault")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PutMapping("/update")
  public ResponseEntity<VaultRequest> updateVault(@AuthenticationPrincipal UserDetails userDetails, @Valid  @RequestBody VaultRequest vault) {
    String username = userDetails.getUsername();
    Vault updatedVault = vaultService.updateVault(username, vault);
    if (updatedVault == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new VaultRequest(new String (updatedVault.getContent(), StandardCharsets.UTF_8)));
  }


  private List<LoginAttemptResponse> convertToDTOs(List<LoginAttempt> loginAttempts) {
    return loginAttempts.stream()
        .map(LoginAttemptResponse::convertToDTO)
        .collect(Collectors.toList());
  }
}