package info.eecc.weather.controller;

import info.eecc.weather.dto.CurrentWeatherDto;
import info.eecc.weather.exception.WeatherException;
import info.eecc.weather.service.WeatherService;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@DisplayName("Weather Controller Tests")
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return weather data when valid coordinates are provided")
    void getCurrentWeather_WithValidCoordinates_ShouldReturnWeatherData() throws Exception {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;
        CurrentWeatherDto mockWeatherDto = createMockCurrentWeatherDto(latitude, longitude);

        when(weatherService.getCurrentWeather(latitude, longitude))
                .thenReturn(mockWeatherDto);

        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", String.valueOf(latitude))
                .param("longitude", String.valueOf(longitude)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.latitude").value(latitude))
                .andExpect(jsonPath("$.longitude").value(longitude))
                .andExpect(jsonPath("$.location").value("52.5200, 13.4050"))
                .andExpect(jsonPath("$.temperature").value(22.5))
                .andExpect(jsonPath("$.temperatureUnit").value("°C"))
                .andExpect(jsonPath("$.humidity").value(65))
                .andExpect(jsonPath("$.apparentTemperature").value(24.2))
                .andExpect(jsonPath("$.weatherDescription").value("Partly cloudy"))
                .andExpect(jsonPath("$.weatherCode").value(2))
                .andExpect(jsonPath("$.windSpeed").value(12.5))
                .andExpect(jsonPath("$.windSpeedUnit").value("km/h"))
                .andExpect(jsonPath("$.windDirection").value(245))
                .andExpect(jsonPath("$.pressure").value(1013.2))
                .andExpect(jsonPath("$.pressureUnit").value("hPa"))
                .andExpect(jsonPath("$.cloudCover").value(75))
                .andExpect(jsonPath("$.day").value(true))
                .andExpect(jsonPath("$.timestamp").value("2024-01-15T14:30:00Z"));

        verify(weatherService, times(1)).getCurrentWeather(latitude, longitude);
    }

    @Test
    @DisplayName("Should return weather data when valid city is provided")
    void getCurrentWeather_WithValidCity_ShouldReturnWeatherData() throws Exception {
        // Given
        String city = "Berlin";
        CurrentWeatherDto mockWeatherDto = createMockCurrentWeatherDto(52.5200, 13.4050);

        when(weatherService.getCurrentWeatherByCity(city))
                .thenReturn(mockWeatherDto);

        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("city", city))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.latitude").value(52.5200))
                .andExpect(jsonPath("$.longitude").value(13.4050))
                .andExpect(jsonPath("$.temperature").value(22.5));

        verify(weatherService, times(1)).getCurrentWeatherByCity(city);
    }

    @Test
    @DisplayName("Should return 400 when no parameters are provided")
    void getCurrentWeather_WithNoParameters_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Either latitude+longitude OR city must be provided"));

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
        verify(weatherService, never()).getCurrentWeatherByCity(any(String.class));
    }

    @Test
    @DisplayName("Should return 400 when both coordinates and city are provided")
    void getCurrentWeather_WithBothCoordinatesAndCity_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", "52.5200")
                .param("longitude", "13.4050")
                .param("city", "Berlin"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Provide either latitude+longitude OR city, not both"));

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
        verify(weatherService, never()).getCurrentWeatherByCity(any(String.class));
    }

    @Test
    @DisplayName("Should return 400 when only latitude is provided")
    void getCurrentWeather_WithOnlyLatitude_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", "52.5200"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Either latitude+longitude OR city must be provided"));

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
        verify(weatherService, never()).getCurrentWeatherByCity(any(String.class));
    }

    @Test
    @DisplayName("Should return 400 when only longitude is provided")
    void getCurrentWeather_WithOnlyLongitude_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("longitude", "13.4050"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Either latitude+longitude OR city must be provided"));

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
        verify(weatherService, never()).getCurrentWeatherByCity(any(String.class));
    }

    @Test
    @DisplayName("Should return 400 when latitude is out of range")
    void getCurrentWeather_WithInvalidLatitude_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", "95.0") // Invalid: > 90
                .param("longitude", "13.4050"))
                .andExpect(status().isBadRequest());

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
    }

    @Test
    @DisplayName("Should return 400 when longitude is out of range")
    void getCurrentWeather_WithInvalidLongitude_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", "52.5200")
                .param("longitude", "185.0")) // Invalid: > 180
                .andExpect(status().isBadRequest());

        verify(weatherService, never()).getCurrentWeather(any(Double.class), any(Double.class));
    }

    @Test
    @DisplayName("Should return 400 when city is empty string")
    void getCurrentWeather_WithEmptyCity_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("city", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Either latitude+longitude OR city must be provided"));

        verify(weatherService, never()).getCurrentWeatherByCity(any(String.class));
    }

    @Test
    @DisplayName("Should return 404 when city is not found")
    void getCurrentWeather_WithNonExistentCity_ShouldReturnNotFound() throws Exception {
        // Given
        String city = "NonExistentCity";
        when(weatherService.getCurrentWeatherByCity(city))
                .thenThrow(new WeatherException(HttpStatus.NOT_FOUND, "City not found: " + city));

        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("city", city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("City not found: " + city));

        verify(weatherService, times(1)).getCurrentWeatherByCity(city);
    }

    @Test
    @DisplayName("Should return 500 when weather service fails")
    void getCurrentWeather_WhenServiceFails_ShouldReturnInternalServerError() throws Exception {
        // Given
        double latitude = 52.5200;
        double longitude = 13.4050;
        when(weatherService.getCurrentWeather(latitude, longitude))
                .thenThrow(new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Weather service temporarily unavailable"));

        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("latitude", String.valueOf(latitude))
                .param("longitude", String.valueOf(longitude)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Weather service temporarily unavailable"));

        verify(weatherService, times(1)).getCurrentWeather(latitude, longitude);
    }

    @Test
    @DisplayName("Should handle service exception with cause")
    void getCurrentWeather_WhenServiceFailsWithCause_ShouldReturnInternalServerError() throws Exception {
        // Given
        String city = "Berlin";
        RuntimeException cause = new RuntimeException("Network timeout");
        WeatherException weatherException = new WeatherException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to fetch weather data", cause);

        when(weatherService.getCurrentWeatherByCity(city))
                .thenThrow(weatherException);

        // When & Then
        mockMvc.perform(get("/api/weather/current")
                .param("city", city))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to fetch weather data"));

        verify(weatherService, times(1)).getCurrentWeatherByCity(city);
    }

    private CurrentWeatherDto createMockCurrentWeatherDto(double latitude, double longitude) {
        return CurrentWeatherDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .location(String.format("%.4f, %.4f", latitude, longitude))
                .temperature(22.5)
                .temperatureUnit("°C")
                .humidity(65)
                .apparentTemperature(24.2)
                .weatherDescription("Partly cloudy")
                .weatherCode(2)
                .windSpeed(12.5)
                .windSpeedUnit("km/h")
                .windDirection(245)
                .pressure(1013.2)
                .pressureUnit("hPa")
                .cloudCover(75)
                .isDay(true)
                .timestamp("2024-01-15T14:30:00Z")
                .build();
    }
}
