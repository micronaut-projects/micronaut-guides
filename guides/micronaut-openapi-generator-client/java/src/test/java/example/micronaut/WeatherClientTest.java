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
package example.micronaut;
import com.sun.net.httpserver.HttpServer;
import example.openmeteo.api.WeatherForecastApisApi;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

// tag::test[]
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherClientTest implements TestPropertyProvider {
    private static HttpServer server;

    @Test
    @DisplayName("Fetches weather for Montaigu-Vendée")
    void fetchesWeather(WeatherForecastApisApi api) {               // <1>
        var forecast = api.v1ForecastGet(46.97386f, -1.3111076f,    // <2>
                null,
                null,
                true,
                null,
                null,
                null,
                null,
                null);
        var weather = forecast.block().getCurrentWeather();        // <3>
        assertTrue(weather.getTemperature() < 50);
    }

    @Override
    public Map<String, String> getProperties() {
        return Map.of("openapi-micronaut-client.base-path", startServer());
    }

    private static synchronized String startServer() {
        if (server != null) {
            return "http://localhost:" + server.getAddress().getPort();
        }
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/v1/forecast", exchange -> {
                byte[] json = """
                    {
                      "latitude": 46.97386,
                      "longitude": -1.3111076,
                      "generationtime_ms": 0.1,
                      "utc_offset_seconds": 0,
                      "timezone": "GMT",
                      "timezone_abbreviation": "GMT",
                      "elevation": 0,
                      "current_weather": {
                        "temperature": 18.4,
                        "windspeed": 10.0,
                        "winddirection": 180,
                        "weathercode": 0,
                        "is_day": 1,
                        "time": "2026-05-22T00:00"
                      }
                    }
                    """.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.length);
                try (var body = exchange.getResponseBody()) {
                    body.write(json);
                }
            });
            server.start();
            return "http://localhost:" + server.getAddress().getPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }
}
// end::test[]
