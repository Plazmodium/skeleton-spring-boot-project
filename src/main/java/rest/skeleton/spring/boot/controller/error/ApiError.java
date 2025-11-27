package rest.skeleton.spring.boot.controller.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standard API error response model.
 */
@Schema(name = "ApiError", description = "Standardized error response for API failures")
public class ApiError {
    private String code;
    private String message;
    private Map<String, Object> details = new LinkedHashMap<>();
    private Instant timestamp = Instant.now();

    public ApiError() {}

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
