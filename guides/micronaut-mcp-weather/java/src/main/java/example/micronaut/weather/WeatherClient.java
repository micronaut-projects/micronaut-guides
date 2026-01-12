package example.micronaut.weather;

import example.micronaut.weather.model.Alerts;
import example.micronaut.weather.model.Forecast;
import example.micronaut.weather.model.Location;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;

import java.util.Map;
import java.util.Optional;

@Client(id = "weather")
@Header(name = HttpHeaders.USER_AGENT, value = "MCP Server")
public interface WeatherClient {
    @Get("/alerts/active/area/{state}")
    Alerts getAlerts(@PathVariable String state);

    @Get("/points/{latitude},{longitude}")
    Map<String, Object> getPoints(@PathVariable double latitude, @PathVariable double longitude);

    @Get("/gridpoints/{gridId}/{gridX},{gridY}/forecast")
    Forecast getForecast(@PathVariable String gridId,
                         @PathVariable String gridX,
                         @PathVariable String gridY);

    default String formattedAlerts(String state) {
        return WeatherUtils.formatAlerts(getAlerts(state));
    }

    default String formattedForecast(double latitude, double longitude) {
        return getForecast(latitude, longitude).map(WeatherUtils::formatForecast).orElse("No forecast available for requested location");
    }

    default Optional<Forecast> getForecast(double latitude, double longitude) {
        Map<String, Object> points = getPoints(latitude, longitude);
        if (CollectionUtils.isEmpty(points)) {
            return Optional.empty();
        }
        return WeatherUtils.getForecastUrl(points)
            .flatMap(WeatherUtils::parseGrid)
            .map(grid -> getForecast(grid.id(), grid.x(), grid.y()));
    }

    default String formattedForecast(Location location) {
        return formattedForecast(location.latitude(), location.longitude());
    }
}
