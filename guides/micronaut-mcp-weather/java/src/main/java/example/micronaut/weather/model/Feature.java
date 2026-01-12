package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record Feature(
    String id,
    String type,
    Object geometry,
    Properties properties) {
}
