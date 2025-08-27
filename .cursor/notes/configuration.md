# Configuration and Deployment

## Application Configuration

### application.yaml

**Location**: `src/main/resources/application.yaml`

**Key Settings**:

- **Server Port**: 8080
- **Application Name**: weather-app
- **Logging**: INFO level for application and Spring Web
- **Actuator**: Health, info, metrics endpoints exposed

### OpenAPI Configuration

- **Swagger UI**: `/swagger-ui.html`
- **API Docs**: `/v3/api-docs`
- **Features**: Method sorting, tag sorting, actuator integration

### Spring Configuration

#### AppConfig

**Location**: `info.eecc.weather.config.AppConfig`
**Beans**:

- `RestTemplate`: HTTP client for external API calls

#### OpenApiConfig

**Location**: `info.eecc.weather.config.OpenApiConfig`
**Purpose**: OpenAPI documentation configuration

## Maven Configuration

### pom.xml Structure

- **Parent**: Spring Boot 3.5.5
- **Java Version**: 24
- **Group**: info.eecc
- **Artifact**: weather
- **Version**: 0.0.1-SNAPSHOT

### Dependencies

- **Core**: Spring Boot Web, Actuator, Validation
- **Utilities**: Lombok, Configuration Processor
- **Documentation**: SpringDoc OpenAPI
- **Testing**: Spring Boot Test

### Build Configuration

- **Compiler**: Annotation processing for Lombok and Spring
- **Packaging**: Executable JAR with Spring Boot plugin
- **Docker**: Cloud Native Buildpacks with Paketo

## Deployment Strategy

### Docker Image

- **Name**: `info.eecc/weather:0.0.1-SNAPSHOT`
- **Buildpack**: Paketo Java buildpack
- **Profile**: Production profile activated
- **Runtime**: Java 24

### Testing

- **Script**: `test-api.sh` for API validation
- **Coverage**: All endpoints and error scenarios
- **Tools**: curl + jq for JSON formatting

### Monitoring

- **Actuator Endpoints**: `/actuator/health`, `/actuator/info`, `/actuator/metrics`
- **Logging**: Structured logging with correlation
