# Error Handling Strategy

## Global Exception Handler

### GlobalExceptionHandler

**Location**: `info.eecc.weather.exception.GlobalExceptionHandler`
**Annotation**: `@RestControllerAdvice`

### Exception Types Handled

#### WeatherException

- **Status**: 400 (Bad Request) or 404 (Not Found)
- **Logic**: Status determined by error message content
- **Use Cases**: Business logic errors, validation failures, city not found

#### ConstraintViolationException

- **Status**: 400 (Bad Request)
- **Source**: Bean validation failures (coordinate ranges)
- **Response**: Validation error message

#### RestClientException

- **Status**: 500 (Internal Server Error)
- **Source**: External API communication failures
- **Response**: Generic "service unavailable" message

#### Generic Exception

- **Status**: 500 (Internal Server Error)
- **Purpose**: Catch-all for unexpected errors
- **Response**: Generic "internal server error"

## Error Response Format

### Consistent Structure

All errors return:

```json
{
  "error": "Human readable error message"
}
```

### HTTP Status Code Strategy

- **400**: Client errors (validation, missing parameters)
- **404**: Resource not found (city lookup failures)
- **500**: Server errors (external service issues, unexpected errors)

## Validation Strategy

### Input Validation

1. **Bean Validation**: `@DecimalMin`/`@DecimalMax` for coordinates
2. **Controller Logic**: Mutual exclusion validation (coordinates vs city)
3. **Service Logic**: Business rule validation

### Error Messages

- **User-Friendly**: Clear, actionable error descriptions
- **No Internal Details**: External API errors abstracted
- **Consistent Format**: All errors follow same JSON structure

## Logging Strategy

### Error Logging

- **Level**: ERROR for all caught exceptions
- **Content**: Original error message and context
- **Security**: Sensitive details not exposed to client

### Request Logging

- **Level**: INFO for successful requests
- **Content**: Coordinates or city name being processed
