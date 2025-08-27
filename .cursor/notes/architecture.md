# Architecture Overview

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Java Version**: 24
- **Build Tool**: Maven
- **Key Dependencies**:
  - Spring Web (REST API)
  - Spring Actuator (Health endpoints)
  - Spring Validation (Request validation)
  - Lombok (Code generation)
  - SpringDoc OpenAPI (API documentation)

## Application Structure

### Layered Architecture

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
External API Integration (Open-Meteo)
```

### Package Organization

- `info.eecc.weather` (root package)
  - `controller/` - REST endpoints
  - `service/` - Core business logic
  - `dto/` - Data transfer objects
  - `config/` - Spring configuration beans
  - `exception/` - Custom exceptions and global error handling

## Key Design Patterns

1. **Repository Pattern**: Service layer abstracts external API calls
2. **DTO Pattern**: Clear separation between internal models and API responses
3. **Builder Pattern**: Used in DTOs for object construction (Lombok)
4. **Global Exception Handling**: Centralized error handling with `@RestControllerAdvice`

## Configuration Strategy

- **YAML Configuration**: `application.yaml` for all settings
- **Profiles**: Production profile configured for Docker deployment
- **Actuator**: Health, info, and metrics endpoints exposed
- **OpenAPI**: Automatic API documentation generation

## Deployment

- **Docker**: Cloud Native Buildpacks with Paketo
- **Image**: `info.eecc/weather:0.0.1-SNAPSHOT`
- **Port**: 8080
- **Profile**: Production profile for deployment
