package info.eecc.weather.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(WeatherException.class)
	public ResponseEntity<Map<String, String>> handleWeatherException(WeatherException ex) {
		log.error("Weather service error: {}", ex.getMessage());
		Map<String, String> error = new HashMap<>();
		String message = ex.getMessage() != null ? ex.getMessage() : "Weather service error";
		error.put("error", message);

		// Use the HttpStatus from the exception
		HttpStatus status = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(ConstraintViolationException ex) {
		log.error("Validation error: {}", ex.getMessage());
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<Map<String, String>> handleRestClientException(RestClientException ex) {
		log.error("External service error: {}", ex.getMessage());
		Map<String, String> error = new HashMap<>();
		error.put("error", "Weather service temporarily unavailable");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		log.error("Unexpected error: {}", ex.getMessage(), ex);
		Map<String, String> error = new HashMap<>();
		error.put("error", "Internal server error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
