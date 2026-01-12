package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record Properties(
    String id,
    String areaDesc,
    String event,
    String severity,
    String description,
    String instruction) {
}
