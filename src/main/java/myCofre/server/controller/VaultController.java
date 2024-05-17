
package myCofre.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import myCofre.server.controller.dto.*;
import myCofre.server.domain.LoginAttempt;
import myCofre.server.service.VaultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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

  @Operation(summary = "Create vault")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PutMapping("/create")
  public ResponseEntity<Void> create(@Valid @RequestBody VaultRequest requestDto) {
    vaultService.createVaultForCurrentUser(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  private List<LoginAttemptResponse> convertToDTOs(List<LoginAttempt> loginAttempts) {
    return loginAttempts.stream()
        .map(LoginAttemptResponse::convertToDTO)
        .collect(Collectors.toList());
  }
}