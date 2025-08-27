package info.eecc.weather.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestClientException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle WeatherException and return appropriate error response")
    void handleWeatherException_ShouldReturnErrorResponse() {
        // Given
        String errorMessage = "City not found: NonExistentCity";
        WeatherException exception = new WeatherException(HttpStatus.NOT_FOUND, errorMessage);

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleWeatherException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Should handle WeatherException with cause and return internal server error")
    void handleWeatherException_WithCause_ShouldReturnInternalServerError() {
        // Given
        String errorMessage = "Failed to fetch weather data";
        RuntimeException cause = new RuntimeException("Network timeout");
        WeatherException exception = new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, cause);

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleWeatherException(exception);

        // Then
        // WeatherExceptions with causes should be treated as server errors
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException and return validation error")
    void handleConstraintViolationException_ShouldReturnValidationError() {
        // Given
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);

        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("latitude");
        when(violation.getMessage()).thenReturn("Latitude must be between -90 and 90");

        ConstraintViolationException exception = new ConstraintViolationException(
                "Validation failed",
                Set.of(violation));

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).contains("Validation failed");
    }

    @Test
    @DisplayName("Should handle generic exception from validation framework")
    void handleValidationFrameworkException_ShouldReturnInternalServerError() {
        // Given
        RuntimeException exception = new RuntimeException("Validation framework error");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Internal server error");
    }

    @Test
    @DisplayName("Should handle RestClientException and return service unavailable error")
    void handleRestClientException_ShouldReturnServiceUnavailableError() {
        // Given
        RestClientException exception = new RestClientException("Connection timeout");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRestClientException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Weather service temporarily unavailable");
    }

    @Test
    @DisplayName("Should handle generic Exception and return internal server error")
    void handleGenericException_ShouldReturnInternalServerError() {
        // Given
        Exception exception = new RuntimeException("Unexpected error occurred");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Internal server error");
    }

    @Test
    @DisplayName("Should handle null exception message gracefully")
    void handleWeatherException_WithNullMessage_ShouldReturnGenericError() {
        // Given
        WeatherException exception = new WeatherException(HttpStatus.BAD_REQUEST, null);

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleWeatherException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Weather service error");
    }
}
