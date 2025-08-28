package info.eecc.weather.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Current weather information for a specific location")
public class CurrentWeatherDto {

	@Schema(description = "Latitude coordinate of the location", example = "52.5200", minimum = "-90", maximum = "90")
	private double latitude;

	@Schema(description = "Longitude coordinate of the location", example = "13.4050", minimum = "-180", maximum = "180")
	private double longitude;

	@Schema(description = "Human-readable location name", example = "Berlin, Germany")
	private String location;

	@Schema(description = "Current temperature", example = "22.5")
	private double temperature;

	@Schema(description = "Temperature unit", example = "°C", allowableValues = { "°C", "°F" })
	private String temperatureUnit;

	@Schema(description = "Relative humidity percentage", example = "65", minimum = "0", maximum = "100")
	private int humidity;

	@Schema(description = "Apparent temperature (feels like)", example = "24.2")
	private double apparentTemperature;

	@Schema(description = "Human-readable weather description", example = "Partly cloudy")
	private String weatherDescription;

	@Schema(description = "WMO weather code", example = "2", minimum = "0", maximum = "99")
	private int weatherCode;

	@Schema(description = "Wind speed", example = "12.5")
	private double windSpeed;

	@Schema(description = "Wind speed unit", example = "km/h", allowableValues = { "km/h", "m/s", "mph" })
	private String windSpeedUnit;

	@Schema(description = "Wind direction in degrees", example = "245", minimum = "0", maximum = "360")
	private int windDirection;

	@Schema(description = "Atmospheric pressure", example = "1013.2")
	private double pressure;

	@Schema(description = "Pressure unit", example = "hPa", allowableValues = { "hPa", "mb", "inHg" })
	private String pressureUnit;

	@Schema(description = "Cloud cover percentage", example = "75", minimum = "0", maximum = "100")
	private int cloudCover;

	@Schema(description = "Whether it's currently day time", example = "true")
	private boolean isDay;

	@Schema(description = "Timestamp of the weather data", example = "2024-01-15T14:30:00Z")
	private String timestamp;

	public static String getWeatherDescription(int weatherCode) {
		return switch (weatherCode) {
			case 0 -> "Clear sky";
			case 1 -> "Mainly clear";
			case 2 -> "Partly cloudy";
			case 3 -> "Overcast";
			case 45, 48 -> "Fog";
			case 51, 53, 55 -> "Drizzle";
			case 56, 57 -> "Freezing drizzle";
			case 61, 63, 65 -> "Rain";
			case 66, 67 -> "Freezing rain";
			case 71, 73, 75 -> "Snow fall";
			case 77 -> "Snow grains";
			case 80, 81, 82 -> "Rain showers";
			case 85, 86 -> "Snow showers";
			case 95 -> "Thunderstorm";
			case 96, 99 -> "Thunderstorm with hail";
			default -> "Unknown";
		};
	}
}
