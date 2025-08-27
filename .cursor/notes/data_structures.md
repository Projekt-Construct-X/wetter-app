# Data Structures

## DTOs (Data Transfer Objects)

### CurrentWeatherDto

**Purpose**: API response format for current weather data

**Key Fields**:

- `latitude`, `longitude`: Location coordinates
- `location`: Human-readable location name
- `temperature`, `temperatureUnit`: Current temperature
- `humidity`: Relative humidity percentage
- `apparentTemperature`: Feels-like temperature
- `weatherDescription`: Human-readable weather condition
- `weatherCode`: WMO weather code (0-99)
- `windSpeed`, `windSpeedUnit`: Wind information
- `windDirection`: Wind direction in degrees
- `pressure`, `pressureUnit`: Atmospheric pressure
- `cloudCover`: Cloud coverage percentage
- `isDay`: Boolean indicating day/night
- `timestamp`: Data timestamp

**Annotations**:

- `@Schema`: OpenAPI documentation
- `@Builder`: Lombok builder pattern
- `@Data`: Lombok getters/setters

### WeatherResponse

**Purpose**: Maps Open-Meteo API response structure

**Structure**:

- Root: `latitude`, `longitude`, `current`, `current_units`
- `CurrentWeather`: Nested class with raw API data
- `CurrentWeatherUnits`: Nested class with unit information

**JSON Mapping**:

- `@JsonProperty("current")`: Maps to `currentWeather`
- `@JsonProperty("current_units")`: Maps to `currentWeatherUnits`

### GeocodingResponse

**Purpose**: Maps geocoding API response

**Structure**:

- `results`: List of location results
- `Result`: Nested class with `latitude`, `longitude`, location info

## Data Mapping Strategy

### Weather Response Mapping

`WeatherService.mapToCurrentWeatherDto()`:

1. Extract raw values from `WeatherResponse.CurrentWeather`
2. Extract units from `WeatherResponse.CurrentWeatherUnits`
3. Map weather code to description using static method
4. Build `CurrentWeatherDto` with proper units

### Weather Code Translation

`CurrentWeatherDto.getWeatherDescription()`:

- Switch statement mapping WMO codes to readable descriptions
- Handles all standard weather conditions
- Returns "Unknown" for unrecognized codes

## Validation

### Input Validation

- Coordinate ranges enforced with `@DecimalMin`/`@DecimalMax`
- City name checked for non-empty string
- Mutual exclusion logic in controller

### Response Validation

- Null checks for external API responses
- Default unit fallbacks when units not provided
- Exception thrown when no data available
