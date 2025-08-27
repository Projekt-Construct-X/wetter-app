# Changelog

## WIP

- Comprehensive unit test suite with mocked API responses

  - Added Mockito dependency for API mocking
  - Created `WeatherServiceTest` with mocked RestTemplate for unit testing external API calls
  - Created `WeatherControllerTest` with MockMvc for testing REST endpoints with mocked service layer
  - Created `GlobalExceptionHandlerTest` for testing error handling scenarios
  - Tests cover success scenarios, error conditions, validation failures, and API errors

- Initial comprehensive notes documentation
  - Created `.cursor/notes/` directory with complete project documentation
  - Added `index.md` with project overview and navigation
  - Added `architecture.md` with technology stack and design patterns
  - Added `api_design.md` with REST endpoint specifications and OpenAPI details
  - Added `external_services.md` with Open-Meteo API integration documentation
  - Added `data_structures.md` with DTO mapping and validation details
  - Added `error_handling.md` with exception handling strategy
  - Added `configuration.md` with deployment and build configuration

## 0.0.1-SNAPSHOT (Initial Release)

- Core weather API functionality

  - REST endpoint `/api/weather/current` for weather data retrieval
  - Support for weather queries by geographic coordinates (latitude/longitude)
  - Support for weather queries by city name with automatic geocoding
  - Integration with Open-Meteo Weather API for current weather data
  - Integration with Open-Meteo Geocoding API for city-to-coordinates conversion

- API features

  - Comprehensive input validation with coordinate range checking
  - Mutual exclusion validation (coordinates OR city, not both)
  - OpenAPI/Swagger documentation with interactive UI
  - Detailed error responses with appropriate HTTP status codes
  - Weather code translation to human-readable descriptions

- Technical implementation

  - Spring Boot 3.5.5 with Java 24
  - Lombok for reduced boilerplate code
  - Global exception handling with `@RestControllerAdvice`
  - RESTTemplate for external API communication
  - Bean validation for request parameters
  - Docker support with Cloud Native Buildpacks

- Data structures

  - `CurrentWeatherDto` with comprehensive weather information
  - `WeatherResponse` for Open-Meteo API mapping
  - `GeocodingResponse` for location lookup
  - WMO weather code mapping to descriptions

- Configuration and monitoring

  - Spring Actuator with health, info, and metrics endpoints
  - Structured YAML configuration
  - Production-ready Docker image configuration
  - Comprehensive test script for API validation

- Documentation
  - OpenAPI 3.0 specification with examples
  - Swagger UI for interactive API testing
  - Error response documentation with examples
  - Test script (`test-api.sh`) for API validation
