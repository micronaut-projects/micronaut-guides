package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public record ForecastProperties(
    List<Period> periods) {
}
