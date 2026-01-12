package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public record Alerts(
    List<String> context,
    String type,
    List<Feature> features,
    String title,
    String updated) {
}
