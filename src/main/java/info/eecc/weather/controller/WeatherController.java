package info.eecc.weather.controller;

import info.eecc.weather.dto.CurrentWeatherDto;
import info.eecc.weather.exception.WeatherException;
import info.eecc.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Weather", description = "Weather API endpoints for retrieving current weather information")
public class WeatherController {

	private final WeatherService weatherService;

	@Operation(summary = "Get current weather information", description = """
			Retrieve current weather data by providing either geographic coordinates (latitude/longitude) or a city name.
			You must provide either coordinates OR city name, but not both.

			Examples:
			• By Coordinates: latitude=52.5200&longitude=13.4050
			• By City Name:   city=Berlin
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Weather data retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrentWeatherDto.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - invalid parameters or missing required data", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(name = "Missing Parameters", value = "{\"error\": \"Either latitude+longitude OR city must be provided\"}"),
					@ExampleObject(name = "Both Parameters", value = "{\"error\": \"Provide either latitude+longitude OR city, not both\"}"),
					@ExampleObject(name = "Invalid Coordinates", value = "{\"error\": \"Latitude must be between -90 and 90\"}")
			})),
			@ApiResponse(responseCode = "404", description = "City not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"City not found: InvalidCityName\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error - weather service unavailable", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Weather service temporarily unavailable\"}")))
	})
	@GetMapping("/current")
	public ResponseEntity<CurrentWeatherDto> getCurrentWeather(
			@Parameter(description = "Latitude coordinate (-90 to 90 degrees). Required if city is not provided.", example = "52.5200", schema = @Schema(minimum = "-90", maximum = "90")) @RequestParam(required = false) @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90") @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90") Double latitude,

			@Parameter(description = "Longitude coordinate (-180 to 180 degrees). Required if city is not provided.", example = "13.4050", schema = @Schema(minimum = "-180", maximum = "180")) @RequestParam(required = false) @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180") @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180") Double longitude,

			@Parameter(description = "Name of the city (e.g., 'Berlin', 'New York', 'Tokyo'). Required if coordinates are not provided.", example = "Berlin") @RequestParam(required = false) String city) {

		// Validate: either coordinates OR city, but not both
		boolean hasCoordinates = latitude != null && longitude != null;
		boolean hasCity = city != null && !city.trim().isEmpty();

		if (!hasCoordinates && !hasCity) {
			throw new WeatherException(HttpStatus.BAD_REQUEST, "Either latitude+longitude OR city must be provided");
		}
		if (hasCoordinates && hasCity) {
			throw new WeatherException(HttpStatus.BAD_REQUEST, "Provide either latitude+longitude OR city, not both");
		}
		if (hasCoordinates && (latitude == null || longitude == null)) {
			throw new WeatherException(HttpStatus.BAD_REQUEST,
					"Both latitude and longitude must be provided when using coordinates");
		}

		CurrentWeatherDto weather;
		if (hasCoordinates) {
			log.info("Fetching current weather for coordinates: lat={}, lon={}", latitude, longitude);
			// Null checks to prevent auto-unboxing issues
			if (latitude != null && longitude != null) {
				weather = weatherService.getCurrentWeather(latitude, longitude);
			} else {
				throw new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Internal error: coordinates validation failed");
			}
		} else {
			log.info("Fetching current weather for city: {}", city);
			weather = weatherService.getCurrentWeatherByCity(city);
		}

		return ResponseEntity.ok(weather);
	}
}
