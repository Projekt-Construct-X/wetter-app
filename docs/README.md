# Weather App Notes Index

This directory contains notes about the Weather App project structure and components.

## Project Overview

The Weather App is a Spring Boot REST API that provides current weather information. It acts as a wrapper around the Open-Meteo weather API and supports querying weather data by either geographic coordinates or city name.

## Project Structure

- **Root**: Maven-based Spring Boot application (Java 24)
- **Package**: `info.eecc.weather`
- **Main artifact**: `weather-0.0.1-SNAPSHOT.jar`

## Notes Files

- [`architecture.md`](./architecture.md) - Overall application architecture and technology stack
- [`api_design.md`](./api_design.md) - REST API design, endpoints, and OpenAPI documentation
- [`external_services.md`](./external_services.md) - Integration with Open-Meteo APIs
- [`data_structures.md`](./data_structures.md) - DTOs and data mapping
- [`error_handling.md`](./error_handling.md) - Exception handling and validation strategy
- [`configuration.md`](./configuration.md) - Application configuration and deployment

## Key Components

### Core Packages

- `controller/` - REST endpoints (`WeatherController`)
- `service/` - Business logic (`WeatherService`)
- `dto/` - Data transfer objects
- `config/` - Spring configuration
- `exception/` - Custom exceptions and global error handling

### External Dependencies

- Open-Meteo Weather API (`api.open-meteo.com`)
- Open-Meteo Geocoding API (`geocoding-api.open-meteo.com`)

## Quick References

- **Main endpoint**: `/api/weather/current`
- **Query methods**: By coordinates (`lat`/`lon`) OR by city name
- **Documentation**: Swagger UI at `/swagger-ui.html`
- **Test script**: `test-api.sh` for API testing
- **Port**: 8080 (default)
