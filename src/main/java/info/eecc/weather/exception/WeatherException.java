package info.eecc.weather.exception;

import org.springframework.http.HttpStatus;

public class WeatherException extends RuntimeException {

	private final HttpStatus httpStatus;

	public WeatherException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public WeatherException(HttpStatus httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
