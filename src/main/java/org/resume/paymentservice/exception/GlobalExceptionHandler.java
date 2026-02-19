package org.resume.paymentservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========== BUSINESS ==========
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<Void> handleNotFound(NotFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
        return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse<Void> handleAlreadyExists(AlreadyExistsException e) {
        log.warn("Resource already exists: {}", e.getMessage());
        return errorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleVerification(VerificationException e) {
        log.warn("Verification failed: {}", e.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Void> handleAuth(AuthException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        return errorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handlePayment(PaymentException e) {
        log.warn("Payment error: identifier={}, reason={}", e.getIdentifier(), e.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    // ========== SECURITY ==========
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResponse<Void> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return errorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // ========== WEBHOOK ==========
    @ExceptionHandler(WebhookProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleWebhookProcessing(WebhookProcessingException e) {
        log.error("Webhook processing failed: eventId={}, reason={}", e.getEventId(), e.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // ========== STRIPE (external service) ==========
    @ExceptionHandler(StripePaymentException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public CommonResponse<Void> handleStripePayment(StripePaymentException e) {
        log.error("Stripe error: {}", e.getStripeError(), e);
        return errorResponse(HttpStatus.BAD_GATEWAY, "Payment provider error. Please try again later.");
    }

    // ========== VALIDATION ==========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        log.warn("Validation failed: {}", message);
        return errorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation failed");

        log.warn("Constraint violation: {}", message);
        return errorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleMissingParameter(MissingServletRequestParameterException e) {
        log.warn("Missing parameter: {}", e.getParameterName());
        return errorResponse(HttpStatus.BAD_REQUEST, "Missing required parameter: " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String requiredType = Objects.requireNonNull(e.getRequiredType()).getSimpleName();
        log.warn("Type mismatch: parameter '{}' expected {}", e.getName(), requiredType);
        return errorResponse(HttpStatus.BAD_REQUEST,
                String.format("Parameter '%s' must be of type %s", e.getName(), requiredType));
    }

    // ========== DATABASE ==========
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse<Void> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("Data integrity violation: {}", e.getMessage());
        return errorResponse(HttpStatus.CONFLICT, "Data conflict. Please check your request.");
    }

    // ========== GENERAL ==========
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Void> handleIllegalState(IllegalStateException e) {
        log.error("Illegal state: {}", e.getMessage(), e);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Void> handleGeneral(Exception e) {
        log.error("Unexpected error", e);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    // ========== HELPER ==========
    private CommonResponse<Void> errorResponse(HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        return CommonResponse.error(problemDetail);
    }

}
