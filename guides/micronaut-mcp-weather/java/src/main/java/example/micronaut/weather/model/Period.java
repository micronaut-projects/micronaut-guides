package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record Period(
    String name,
    int temperature,
    String temperatureUnit,
    String windSpeed,
    String windDirection,
    String detailedForecast) {
}
