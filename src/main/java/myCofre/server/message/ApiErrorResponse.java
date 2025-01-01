package myCofre.server.message;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErrorResponse {

    @Schema(description = "Timestamp indicating when the error occurred, in ISO-8601 format.")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code associated with the error (e.g., 400, 404, 500).")
    private int status;

    @Schema(description = "Short title or name of the error (e.g., 'Not Found', 'Bad Request').")
    private String error;

    @Schema(description = "Detailed message explaining the cause of the error.")
    private String message;

    @Schema(description = "The API endpoint path where the error occurred.")
    private String path;

    @Schema(description = "List of specific error details, often used for validation errors.")
    private List<String> errors;

    public ApiErrorResponse(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = getErrorNameFromStatus(status);
        this.message = message;
        this.path = "Unknown";
    }

    public ApiErrorResponse(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = getErrorNameFromStatus(status);
        this.message = message;
        this.path = path;
    }

    public ApiErrorResponse(int status, String error, String message, String path, List<String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    private String getErrorNameFromStatus(int status) {
        switch (status) {
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 409: return "Conflict";
            case 500: return "Internal Server Error";
            default: return "Error";
        }
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ApiErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", errors=" + errors +
                '}';
    }
}
