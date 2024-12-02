
package myCofre.server.controller.auth;

import myCofre.server.helper.JwtHelper;
import myCofre.server.controller.ApiErrorResponse;
import myCofre.server.domain.LoginAttempt;
import myCofre.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/myCofre-api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final AuthService authService;

  private final PasswordEncoder passwordEncoder;

  public AuthController(AuthenticationManager authenticationManager, AuthService authService, PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.authService = authService;
    this.passwordEncoder = passwordEncoder;
  }

  @Operation(summary = "Authenticate user and return token")
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.repassword()));
    } catch (BadCredentialsException e) {
      authService.addLoginAttempt(request.email(), false);
      throw e;
    }
    String state = authService.findUserByEmail(request.email()).getAccountState();
    if(!state.equalsIgnoreCase("A")){
      throw new BadCredentialsException("User not activated");
    }
    String token = JwtHelper.generateToken(request.email());
    authService.addLoginAttempt(request.email(), true);
    return ResponseEntity.ok(new LoginResponse(token));
  }

  @Operation(summary = "Change user repassword")
  @ApiResponse(responseCode = "202")
  @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PatchMapping(value = "/changeRepassword")
  public ResponseEntity<Void> changeRepassword(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangePasswordRequest request) {
    String email = JwtHelper.extractUsername(token.replace("Bearer ", ""));
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.oldRepassword()));
    String hashedRepassword = passwordEncoder.encode(request.newRepassword());
    authService.changePassword(email, hashedRepassword);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @Operation(summary = "Get recent login attempts")
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginAttemptResponse.class)))
  @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))//forbidden
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @GetMapping(value = "/getLoginAttempts")
  public ResponseEntity<List<LoginAttemptResponse>> getLoginAttempts(@RequestHeader("Authorization") String token) {
    String email = JwtHelper.extractUsername(token.replace("Bearer ", ""));
    List<LoginAttempt> loginAttempts = authService.findRecentLoginAttempts(email);
    return ResponseEntity.ok(loginAttemptsEntityToResponse(loginAttempts));
  }

  private List<LoginAttemptResponse> loginAttemptsEntityToResponse(List<LoginAttempt> loginAttempts) {
    return loginAttempts.stream()
        .map(LoginAttemptResponse::convertToResponse)
        .collect(Collectors.toList());
  }
}