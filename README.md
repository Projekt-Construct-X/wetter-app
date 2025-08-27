# Weather App

A Spring Boot REST API that provides current weather information by wrapping the Open-Meteo weather service. Supports weather queries by geographic coordinates or city name with comprehensive validation and error handling.

## About

This application serves as a weather data API wrapper that:

- Fetches current weather data from Open-Meteo API
- Supports queries by coordinates (latitude/longitude) or city name
- Provides automatic geocoding for city names
- Returns structured weather information with human-readable descriptions
- Includes comprehensive API documentation via Swagger UI

## Features

- **Flexible Querying**: Get weather by coordinates OR city name
- **Input Validation**: Comprehensive validation with clear error messages
- **Weather Codes**: WMO weather codes translated to human-readable descriptions
- **API Documentation**: Interactive Swagger UI for testing and exploration
- **Error Handling**: Robust error handling with appropriate HTTP status codes
- **Monitoring**: Spring Actuator endpoints for health and metrics
- **Docker Ready**: Cloud Native Buildpacks for containerized deployment

## Documentation

- **API Documentation**: Available at `/swagger-ui.html` when running
- **Technical Notes**: Detailed documentation in `.cursor/notes/` directory
- **API Testing**: Use the provided `test-api.sh` script

## Getting Started

### Prerequisites

To run and work with this project, you need:

- Java 24 or later
- Maven 3.6+
- Internet connection (for external weather API calls)

### Installation

1. Clone this repository

   ```sh
   git clone https://github.com/Projekt-Construct-X/wetter-app.git
   ```

2. Navigate to the project directory

   ```sh
   cd wetter-app
   ```

3. Run the application

   ```sh
   mvn spring-boot:run
   ```

   Or build and run the JAR:

   ```sh
   mvn clean package
   java -jar target/weather-0.0.1-SNAPSHOT.jar
   ```

4. Access the application:
   - **API Base URL**: http://localhost:8080/api/weather
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **Health Check**: http://localhost:8080/actuator/health

### Docker Deployment

Build and run with Docker:

```sh
mvn spring-boot:build-image
docker run -p 8080:8080 info.eecc/weather:0.0.1-SNAPSHOT
```

## API Usage

### Get Weather by Coordinates

```sh
curl "http://localhost:8080/api/weather/current?latitude=52.52&longitude=13.405"
```

### Get Weather by City Name

```sh
curl "http://localhost:8080/api/weather/current?city=Berlin"
```

### Test the API

Run the comprehensive test script:

```sh
./test-api.sh
```

## Response Example

```json
{
  "latitude": 52.52,
  "longitude": 13.405,
  "location": "Berlin, Germany",
  "temperature": 22.5,
  "temperatureUnit": "°C",
  "humidity": 65,
  "apparentTemperature": 24.2,
  "weatherDescription": "Partly cloudy",
  "weatherCode": 2,
  "windSpeed": 12.5,
  "windSpeedUnit": "km/h",
  "windDirection": 245,
  "pressure": 1013.2,
  "pressureUnit": "hPa",
  "cloudCover": 75,
  "isDay": true,
  "timestamp": "2024-01-15T14:30:00Z"
}
```

## License

All code files are distributed under the Apache 2.0 license. See [LICENSE](./LICENSE) for more information.

All non-code files are distributed under the Creative Commons Attribution 4.0 International license. See [LICENSE_non-code](./LICENSE_non-code) for more information.
