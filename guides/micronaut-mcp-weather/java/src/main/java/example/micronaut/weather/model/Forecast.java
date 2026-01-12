package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record Forecast(ForecastProperties properties) {
}
