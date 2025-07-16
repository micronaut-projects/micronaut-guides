/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherClientTest {
    @Test
    void testClient() {
        try (EmbeddedServer openWeatherMock = ApplicationContext.run(EmbeddedServer.class,
                Map.of("spec.name", "openweathermock"))) {
            try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class,
                    Map.of("micronaut.http.services.openweather.url",
                            "http://localhost:" + openWeatherMock.getPort()))) {
                WeatherClient client = server.getApplicationContext().getBean(WeatherClient.class);
                var response = client.current("44.34", "10.99");
                assertNotNull(response);
                assertTrue(response.main().temp() > -10);
                assertEquals(44.34, response.coord().lat(), 1e-4);
            }
        }
    }

    @Requires(property = "spec.name", value = "openweathermock")
    @Controller
    static class OpenWeatherMockController {
        @Get("/data/2.5/weather")
        String current() {
            return """
                    {"coord":{"lon":10.99,"lat":44.34},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"base":"stations","main":{"temp":284.53,"feels_like":283.19,"temp_min":282.71,"temp_max":284.62,"pressure":1028,"humidity":56,"sea_level":1028,"grnd_level":959},"visibility":10000,"wind":{"speed":2.16,"deg":37,"gust":1.76},"clouds":{"all":45},"dt":1741171548,"sys":{"type":2,"id":2004688,"country":"IT","sunrise":1741153593,"sunset":1741194529},"timezone":3600,"id":3163858,"name":"Zocca","cod":200}                   
                    """;
        }
    }
}
