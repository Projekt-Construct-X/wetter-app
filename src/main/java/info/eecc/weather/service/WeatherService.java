package info.eecc.weather.service;

import info.eecc.weather.dto.CurrentWeatherDto;
import info.eecc.weather.dto.GeocodingResponse;
import info.eecc.weather.dto.WeatherResponse;
import info.eecc.weather.exception.WeatherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

	private final RestTemplate restTemplate;

	private static final String GEOCODING_API_URL = "https://geocoding-api.open-meteo.com/v1/search";
	private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";

	public CurrentWeatherDto getCurrentWeather(double latitude, double longitude) {
		log.info("Fetching weather data for coordinates: lat={}, lon={}", latitude, longitude);

		try {
			String url = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
					.queryParam("latitude", latitude)
					.queryParam("longitude", longitude)
					.queryParam("current",
							"temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,wind_speed_10m,wind_direction_10m,surface_pressure,cloud_cover")
					.toUriString();

			WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

			if (response == null || response.getCurrentWeather() == null) {
				throw new WeatherException(HttpStatus.NOT_FOUND,
						"No weather data available for the specified coordinates");
			}

			return mapToCurrentWeatherDto(response, String.format("%.4f, %.4f", latitude, longitude));

		} catch (Exception e) {
			log.error("Error fetching weather data for coordinates lat={}, lon={}: {}", latitude, longitude,
					e.getMessage());
			throw new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to fetch weather data: " + e.getMessage(), e);
		}
	}

	public CurrentWeatherDto getCurrentWeatherByCity(String city) {
		log.info("Fetching weather data for city: {}", city);

		try {
			// First, get coordinates for the city
			GeocodingResponse.Result location = getCoordinatesForCity(city);

			// Then get weather data
			return getCurrentWeather(location.getLatitude(), location.getLongitude());

		} catch (Exception e) {
			log.error("Error fetching weather data for city {}: {}", city, e.getMessage());
			throw new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to fetch weather data for city: " + city, e);
		}
	}

	private GeocodingResponse.Result getCoordinatesForCity(String city) {
		String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
				.queryParam("name", city)
				.queryParam("count", 1)
				.queryParam("language", "en")
				.queryParam("format", "json")
				.toUriString();

		GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);

		if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
			throw new WeatherException(HttpStatus.NOT_FOUND, "City not found: " + city);
		}

		return response.getResults().get(0);
	}

	private CurrentWeatherDto mapToCurrentWeatherDto(WeatherResponse response, String location) {
		WeatherResponse.CurrentWeather current = response.getCurrentWeather();
		WeatherResponse.CurrentWeatherUnits units = response.getCurrentWeatherUnits();

		return CurrentWeatherDto.builder()
				.latitude(response.getLatitude())
				.longitude(response.getLongitude())
				.location(location)
				.temperature(current.getTemperature_2m())
				.temperatureUnit(units != null ? units.getTemperature_2m() : "°C")
				.humidity(current.getRelative_humidity_2m())
				.apparentTemperature(current.getApparent_temperature())
				.weatherDescription(CurrentWeatherDto.getWeatherDescription(current.getWeather_code()))
				.weatherCode(current.getWeather_code())
				.windSpeed(current.getWind_speed_10m())
				.windSpeedUnit(units != null ? units.getWind_speed_10m() : "km/h")
				.windDirection(current.getWind_direction_10m())
				.pressure(current.getSurface_pressure())
				.pressureUnit(units != null ? units.getSurface_pressure() : "hPa")
				.cloudCover(current.getCloud_cover())
				.isDay(current.getIs_day() == 1)
				.timestamp(current.getTime())
				.build();
	}
}
