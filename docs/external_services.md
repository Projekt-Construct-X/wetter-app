# External Services Integration

## Open-Meteo APIs

### Weather API

- **URL**: `https://api.open-meteo.com/v1/forecast`
- **Purpose**: Retrieve current weather data
- **Method**: GET

**Parameters**:

- `latitude`, `longitude`: Coordinates
- `current`: Weather parameters to fetch
  - `temperature_2m`: Temperature at 2 meters
  - `relative_humidity_2m`: Humidity percentage
  - `apparent_temperature`: Feels-like temperature
  - `is_day`: Day/night indicator
  - `weather_code`: WMO weather code
  - `wind_speed_10m`: Wind speed at 10 meters
  - `wind_direction_10m`: Wind direction in degrees
  - `surface_pressure`: Atmospheric pressure
  - `cloud_cover`: Cloud coverage percentage

### Geocoding API

- **URL**: `https://geocoding-api.open-meteo.com/v1/search`
- **Purpose**: Convert city names to coordinates
- **Method**: GET

**Parameters**:

- `name`: City name to search
- `count`: Number of results (set to 1)
- `language`: Response language (set to "en")
- `format`: Response format (set to "json")

## Integration Strategy

### RestTemplate Configuration

- **Bean**: Configured in `AppConfig.java`
- **Error Handling**: `RestClientException` caught and handled globally

### Service Implementation

1. **Direct Coordinates**: Call weather API directly
2. **City Name**: First geocode to coordinates, then call weather API
3. **Error Mapping**: External errors mapped to domain exceptions

## Data Mapping

### Weather Codes (WMO Standard)

- 0: Clear sky
- 1: Mainly clear
- 2: Partly cloudy
- 3: Overcast
- 45,48: Fog
- 51,53,55: Drizzle variants
- 61,63,65: Rain variants
- 71,73,75: Snow variants
- 95: Thunderstorm
- 96,99: Thunderstorm with hail

### Unit Handling

- **Temperature**: °C (Celsius)
- **Wind Speed**: km/h
- **Pressure**: hPa (hectopascals)
- **Humidity**: % (percentage)

## Error Scenarios

1. **Network Issues**: Service unavailable (500)
2. **Invalid Coordinates**: No data available
3. **City Not Found**: Empty geocoding results (404)
4. **API Limits**: Rate limiting or service degradation
