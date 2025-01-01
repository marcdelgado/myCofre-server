
package myCofre.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import myCofre.server.config.BaseController;
import myCofre.server.domain.User;
import myCofre.server.helper.JwtHelper;
import myCofre.server.message.*;
import myCofre.server.service.AuthService;
import myCofre.server.service.EmailService;
import myCofre.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/myCofre-api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

  private final UserService userService;

  private final EmailService emailService;

  private final AuthService authService;

  public UserController(UserService userService, EmailService emailService, AuthService authService) {
    super();
    this.userService = userService;
    this.emailService = emailService;
    this.authService = authService;
  }

  @Operation(summary = "Signup by user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PutMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) throws MessagingException {
    userService.signup(signupRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "View user profile by user")
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @GetMapping("/view")
  public ResponseEntity<UserResponse> view(@RequestHeader("Authorization") String token) {
    String email = JwtHelper.extractUsername(token.replace("Bearer ", ""));
    User user = userService.view(email);
    return ResponseEntity.ok(UserResponse.convertToResponse(user));
  }

  @Operation(summary = "Edit user profile by user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PatchMapping("/edit")
  public ResponseEntity<Void> edit(@RequestHeader("Authorization") String token, @Valid @RequestBody UserRequest userRequest) {
    String previousEmail = JwtHelper.extractUsername(token.replace("Bearer ", ""));
    userService.edit(previousEmail, userRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @Operation(summary = "Activate user profile by user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PatchMapping("/activate")
  public ResponseEntity<Void> activate(@Valid @RequestBody ActivateRequest activateRequest) {
    userService.activate(activateRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @Operation(summary = "Request delete user by user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PatchMapping("/requestDelete")
  public ResponseEntity<Void> requestDelete(@Valid @RequestBody RequestDeleteRequest request) throws MessagingException {
    userService.requestDelete(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @Operation(summary = "Confirm delete user by user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PatchMapping("/confirmDelete")
  public ResponseEntity<Void> confirmDelete(@Valid @RequestBody DeleteRequest deleteRequest) {
    userService.confirmDelete(deleteRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  private UserResponse userEntityToResponse(User user) {
    return UserResponse.convertToResponse(user);
  }
}