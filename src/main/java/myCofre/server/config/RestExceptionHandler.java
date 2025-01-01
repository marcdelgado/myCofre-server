package myCofre.server.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.mail.MessagingException;
import myCofre.server.message.ApiErrorResponse;
import myCofre.server.exception.DuplicateException;
import myCofre.server.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;

import myCofre.server.exception.OutOfSyncException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
    ApiErrorResponse response = new ApiErrorResponse(NOT_FOUND.value(), e.getMessage());
    logError(response);
    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleRequestNotValidException(MethodArgumentNotValidException e) {
    List<String> errors = new ArrayList<>();
    e.getBindingResult()
            .getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
    e.getBindingResult()
            .getGlobalErrors()
            .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

    String message = "Validation of request failed: %s".formatted(String.join(", ", errors));
    ApiErrorResponse response = new ApiErrorResponse(BAD_REQUEST.value(), message);
    logError(response);
    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleBadCredentialsException() {
    ApiErrorResponse response = new ApiErrorResponse(UNAUTHORIZED.value(), "Invalid username or password");
    logError(response);
    return ResponseEntity.status(UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<ApiErrorResponse> handleDuplicateException(DuplicateException e) {
    ApiErrorResponse response = new ApiErrorResponse(CONFLICT.value(), e.getMessage());
    logError(response);
    return ResponseEntity.status(CONFLICT).body(response);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
    ApiErrorResponse response = new ApiErrorResponse(UNAUTHORIZED.value(), e.getMessage());
    logError(response);
    return ResponseEntity.status(UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(OutOfSyncException.class)
  public ResponseEntity<ApiErrorResponse> handleOutOfSyncException(OutOfSyncException e) {
    ApiErrorResponse response = new ApiErrorResponse(CONFLICT.value(), e.getMessage());
    logError(response);
    return ResponseEntity.status(CONFLICT).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnknownException(Exception e) {
    ApiErrorResponse response = new ApiErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    logError(response);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<ApiErrorResponse> handleMessagingException(MessagingException e) {
    ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error sending email: " + e.getMessage()
    );
    logError(response);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Método que recibe un ApiErrorResponse y lo imprime como un JSON en línea.
   *
   * @param response El objeto ApiErrorResponse que se quiere imprimir.
   */
  private void logError(ApiErrorResponse response) {
    StringBuilder json = new StringBuilder();
    json.append("{")
            .append("\"timestamp\":").append("\"").append(response.getTimestamp()).append("\",")
            .append("\"status\":").append(response.getStatus()).append(",")
            .append("\"error\":").append("\"").append(response.getError()).append("\",")
            .append("\"message\":").append("\"").append(response.getMessage()).append("\",")
            .append("\"path\":").append("\"").append(response.getPath() != null ? response.getPath() : "Unknown").append("\"")
            .append("}");
    logger.error(json.toString());
  }
}
