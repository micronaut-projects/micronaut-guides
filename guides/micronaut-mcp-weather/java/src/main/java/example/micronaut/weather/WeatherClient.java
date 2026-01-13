/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
