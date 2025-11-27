package rest.skeleton.spring.boot.controller.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import rest.skeleton.spring.boot.service.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        ApiError error = new ApiError("validation_error", getMessage("error.validation_failed", "Validation failed"));
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        error.getDetails().put("fields", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        ApiError error = new ApiError("malformed_json", getMessage("error.malformed_json", "Request body is malformed or unreadable"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError error = new ApiError("validation_error", getMessage("error.constraint_violation", "Constraint violation"));
        Map<String, String> violations = new HashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            violations.put(v.getPropertyPath().toString(), v.getMessage());
        }
        error.getDetails().put("violations", violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        // Ideally ResourceNotFoundException would carry a code, but for now we use the exception message
        // or we could use a generic not found message.
        // Let's try to see if we can localize it if it matches a pattern, but for now let's just use a generic localized message + detail?
        // Or just keep ex.getMessage() as the "detail" and use a localized "Resource not found" as message?
        // The original code was: new ApiError("not_found", ex.getMessage());
        // I will change it to: new ApiError("not_found", getMessage("error.not_found", "Resource not found"));
        // And maybe add ex.getMessage() to details if needed, but ApiError structure seems to rely on 'message' as the main info.
        // If ex.getMessage() contains dynamic info (e.g. "User with id 1 not found"), replacing it with static "Resource not found" might be a regression in detail.
        // However, usually "message" in API error should be safe/generic and details can contain specifics.
        // Let's stick to the pattern.
        ApiError error = new ApiError("not_found", getMessage("error.not_found", "Resource not found"));
        error.getDetails().put("description", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError error = new ApiError("invalid_argument", getMessage("error.invalid_argument", "Invalid argument provided"));
        error.getDetails().put("description", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        // Log full stacktrace to aid in diagnosing issues like /v3/api-docs generation failures
        log.error("[GLOBAL-ERROR] Unhandled exception: {}", ex.getMessage(), ex);
        ApiError error = new ApiError("internal_error", getMessage("error.internal_error", "An unexpected error occurred"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
