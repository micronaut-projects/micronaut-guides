package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record Grid(String id, String x, String y) {
}
