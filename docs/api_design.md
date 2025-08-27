# API Design

## REST Endpoints

### Weather Endpoint

- **Path**: `/api/weather/current`
- **Method**: GET
- **Purpose**: Retrieve current weather information

### Parameters

**Mutually Exclusive Options:**

1. **By Coordinates**: `latitude` + `longitude` (both required)
2. **By City**: `city` (string)

### Validation Rules

- Latitude: -90 to 90 degrees
- Longitude: -180 to 180 degrees
- City: Non-empty string
- **Constraint**: Either coordinates OR city, never both

## OpenAPI Documentation

### Swagger UI

- **URL**: `/swagger-ui.html`
- **Features**: Interactive API testing, parameter validation, example requests/responses

### API Docs

- **JSON**: `/v3/api-docs`
- **Configuration**: Comprehensive examples and error cases documented

### Response Examples

**Success (200)**:

```json
{
  "latitude": 52.52,
  "longitude": 13.405,
  "location": "Berlin, Germany",
  "temperature": 22.5,
  "temperatureUnit": "°C",
  "humidity": 65,
  "weatherDescription": "Partly cloudy"
}
```

**Error (400)**:

```json
{
  "error": "Either latitude+longitude OR city must be provided"
}
```

## Error Handling

### HTTP Status Codes

- **200**: Success
- **400**: Bad request (validation errors, missing/conflicting parameters)
- **404**: City not found
- **500**: External service unavailable

### Error Response Format

All errors return:

```json
{
  "error": "Human readable error message"
}
```

## Request Validation

- **Bean Validation**: `@DecimalMin`/`@DecimalMax` for coordinates
- **Custom Logic**: Mutual exclusion validation in controller
- **Global Handling**: `ConstraintViolationException` handled globally
