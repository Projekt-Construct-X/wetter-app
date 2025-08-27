package info.eecc.weather.service;

import info.eecc.weather.dto.CurrentWeatherDto;
import info.eecc.weather.dto.GeocodingResponse;
import info.eecc.weather.dto.WeatherResponse;
import info.eecc.weather.exception.WeatherException;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Weather Service Tests")
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(restTemplate);
    }

    @Test
    @DisplayName("Should successfully get weather by coordinates")
    void getCurrentWeather_WithValidCoordinates_ShouldReturnWeatherData() {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;
        WeatherResponse mockResponse = createMockWeatherResponse(latitude, longitude);

        when(restTemplate.getForObject(any(String.class), eq(WeatherResponse.class)))
                .thenReturn(mockResponse);

        // When
        CurrentWeatherDto result = weatherService.getCurrentWeather(latitude, longitude);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLatitude()).isEqualTo(latitude);
        assertThat(result.getLongitude()).isEqualTo(longitude);
        assertThat(result.getTemperature()).isEqualTo(22.5);
        assertThat(result.getHumidity()).isEqualTo(65);
        assertThat(result.getWeatherDescription()).isEqualTo("Partly cloudy");
        assertThat(result.getWeatherCode()).isEqualTo(2);
        assertThat(result.getWindSpeed()).isEqualTo(12.5);
        assertThat(result.getWindDirection()).isEqualTo(245);
        assertThat(result.getPressure()).isEqualTo(1013.2);
        assertThat(result.getCloudCover()).isEqualTo(75);
        assertThat(result.isDay()).isTrue();
        assertThat(result.getTimestamp()).isEqualTo("2024-01-15T14:30:00Z");

        verify(restTemplate, times(1)).getForObject(any(String.class), eq(WeatherResponse.class));
    }

    @Test
    @DisplayName("Should throw exception when weather API returns null response")
    void getCurrentWeather_WhenApiReturnsNull_ShouldThrowException() {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;

        when(restTemplate.getForObject(any(String.class), eq(WeatherResponse.class)))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeather(latitude, longitude))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("No weather data available for the specified coordinates");
    }

    @Test
    @DisplayName("Should throw exception when weather API returns response with null current weather")
    void getCurrentWeather_WhenCurrentWeatherIsNull_ShouldThrowException() {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setLatitude(latitude);
        mockResponse.setLongitude(longitude);
        mockResponse.setCurrentWeather(null);

        when(restTemplate.getForObject(any(String.class), eq(WeatherResponse.class)))
                .thenReturn(mockResponse);

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeather(latitude, longitude))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("No weather data available for the specified coordinates");
    }

    @Test
    @DisplayName("Should throw exception when weather API call fails")
    void getCurrentWeather_WhenApiCallFails_ShouldThrowException() {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;

        when(restTemplate.getForObject(any(String.class), eq(WeatherResponse.class)))
                .thenThrow(new RestClientException("Connection timeout"));

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeather(latitude, longitude))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("Failed to fetch weather data")
                .hasCauseInstanceOf(RestClientException.class);
    }

    @Test
    @DisplayName("Should successfully get weather by city name")
    void getCurrentWeatherByCity_WithValidCity_ShouldReturnWeatherData() {
        // Given
        String city = "Berlin";
        GeocodingResponse mockGeocodingResponse = createMockGeocodingResponse();
        WeatherResponse mockWeatherResponse = createMockWeatherResponse(52.5200, 13.4050);

        when(restTemplate.getForObject(contains("geocoding-api.open-meteo.com"), eq(GeocodingResponse.class)))
                .thenReturn(mockGeocodingResponse);
        when(restTemplate.getForObject(contains("api.open-meteo.com"), eq(WeatherResponse.class)))
                .thenReturn(mockWeatherResponse);

        // When
        CurrentWeatherDto result = weatherService.getCurrentWeatherByCity(city);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLatitude()).isEqualTo(52.5200);
        assertThat(result.getLongitude()).isEqualTo(13.4050);
        assertThat(result.getTemperature()).isEqualTo(22.5);

        verify(restTemplate, times(1)).getForObject(contains("geocoding-api.open-meteo.com"),
                eq(GeocodingResponse.class));
        verify(restTemplate, times(1)).getForObject(contains("api.open-meteo.com"), eq(WeatherResponse.class));
    }

    @Test
    @DisplayName("Should throw exception when city is not found")
    void getCurrentWeatherByCity_WhenCityNotFound_ShouldThrowException() {
        // Given
        String city = "NonExistentCity";
        GeocodingResponse mockResponse = new GeocodingResponse();
        mockResponse.setResults(List.of()); // Empty results

        when(restTemplate.getForObject(contains("geocoding-api.open-meteo.com"), eq(GeocodingResponse.class)))
                .thenReturn(mockResponse);

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeatherByCity(city))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("Failed to fetch weather data for city: " + city)
                .hasCauseInstanceOf(WeatherException.class);
    }

    @Test
    @DisplayName("Should throw exception when geocoding API returns null")
    void getCurrentWeatherByCity_WhenGeocodingReturnsNull_ShouldThrowException() {
        // Given
        String city = "Berlin";

        when(restTemplate.getForObject(contains("geocoding-api.open-meteo.com"), eq(GeocodingResponse.class)))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeatherByCity(city))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("Failed to fetch weather data for city: " + city)
                .hasCauseInstanceOf(WeatherException.class);
    }

    @Test
    @DisplayName("Should throw exception when geocoding API call fails")
    void getCurrentWeatherByCity_WhenGeocodingApiFails_ShouldThrowException() {
        // Given
        String city = "Berlin";

        when(restTemplate.getForObject(contains("geocoding-api.open-meteo.com"), eq(GeocodingResponse.class)))
                .thenThrow(new RestClientException("Network error"));

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeatherByCity(city))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("Failed to fetch weather data for city: " + city)
                .hasCauseInstanceOf(RestClientException.class);
    }

    @Test
    @DisplayName("Should handle weather API failure after successful geocoding")
    void getCurrentWeatherByCity_WhenWeatherApiFails_ShouldThrowException() {
        // Given
        String city = "Berlin";
        GeocodingResponse mockGeocodingResponse = createMockGeocodingResponse();

        when(restTemplate.getForObject(contains("geocoding-api.open-meteo.com"), eq(GeocodingResponse.class)))
                .thenReturn(mockGeocodingResponse);
        when(restTemplate.getForObject(contains("api.open-meteo.com"), eq(WeatherResponse.class)))
                .thenThrow(new RestClientException("Weather API error"));

        // When & Then
        assertThatThrownBy(() -> weatherService.getCurrentWeatherByCity(city))
                .isInstanceOf(WeatherException.class)
                .hasMessageContaining("Failed to fetch weather data for city: " + city);
    }

    private WeatherResponse createMockWeatherResponse(double lat, double lon) {
        WeatherResponse response = new WeatherResponse();
        response.setLatitude(lat);
        response.setLongitude(lon);

        WeatherResponse.CurrentWeather currentWeather = new WeatherResponse.CurrentWeather();
        currentWeather.setTime("2024-01-15T14:30:00Z");
        currentWeather.setInterval(900);
        currentWeather.setTemperature_2m(22.5);
        currentWeather.setRelative_humidity_2m(65);
        currentWeather.setApparent_temperature(24.2);
        currentWeather.setIs_day(1);
        currentWeather.setWeather_code(2);
        currentWeather.setWind_speed_10m(12.5);
        currentWeather.setWind_direction_10m(245);
        currentWeather.setSurface_pressure(1013.2);
        currentWeather.setCloud_cover(75);

        WeatherResponse.CurrentWeatherUnits units = new WeatherResponse.CurrentWeatherUnits();
        units.setTime("iso8601");
        units.setInterval("seconds");
        units.setTemperature_2m("°C");
        units.setRelative_humidity_2m("%");
        units.setApparent_temperature("°C");
        units.setIs_day("");
        units.setWeather_code("wmo code");
        units.setWind_speed_10m("km/h");
        units.setWind_direction_10m("°");
        units.setSurface_pressure("hPa");
        units.setCloud_cover("%");

        response.setCurrentWeather(currentWeather);
        response.setCurrentWeatherUnits(units);

        return response;
    }

    private GeocodingResponse createMockGeocodingResponse() {
        GeocodingResponse response = new GeocodingResponse();

        GeocodingResponse.Result result = new GeocodingResponse.Result();
        result.setLatitude(52.5200);
        result.setLongitude(13.4050);
        result.setName("Berlin");
        result.setCountry("Germany");
        result.setAdmin1("Berlin");
        result.setAdmin2("");

        response.setResults(List.of(result));
        return response;
    }
}
