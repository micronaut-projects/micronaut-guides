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
import example.micronaut.weather.model.Grid;
import io.micronaut.core.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class WeatherUtils {
    private WeatherUtils() {
    }

    private static final Pattern GRID_URL_PATTERN = Pattern.compile(
        ".*/gridpoints/([A-Z]{3})/(\\d+),(\\d+)/forecast$"
    );

    public static Optional<Grid> parseGrid(String url) {
        Matcher matcher = GRID_URL_PATTERN.matcher(url);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        String id = matcher.group(1);
        String x = matcher.group(2);
        String y = matcher.group(3);
        return Optional.of(new Grid(id, x, y));
    }


    public static Optional<String> getForecastUrl(Map<String, Object> points) {
        Object propertiesObj = points.get("properties");
        if (propertiesObj instanceof Map<?, ?> propertiesMap) {
            Object forecast = propertiesMap.get("forecast");
            if (forecast instanceof String forecastUrl) {
                return Optional.of(forecastUrl);
            }
        }
        return Optional.empty();
    }


    public static String formatForecast(Forecast forecast) {
        return forecast.properties().periods().stream()
            .map(period -> String.format(
                """
                Temperature: %d°%s
                Wind: %s %s
                Forecast: %s
                """,
                period.temperature(),
                period.temperatureUnit(),
                period.windSpeed(),
                period.windDirection(),
                period.detailedForecast()))
            .collect(Collectors.joining("\n---\n"));
    }

    public static String formatAlerts(Alerts alerts) {
        return CollectionUtils.isEmpty(alerts.features())
            ? "There are no watches, warnings or advisories"
            : alerts.features()
                    .stream()
                    .map(feature -> {
                        var p = feature.properties();
                        return String.format("""
                    Event: %s
                    Area: %s
                    Severity: %s
                    Description: %s
                    Instructions: %s""",
                            p.event(),
                            p.areaDesc(),
                            p.severity(),
                            p.description(),
                            p.instruction());
                    })
                    .collect(Collectors.joining("\n---\n"));
    }
}
