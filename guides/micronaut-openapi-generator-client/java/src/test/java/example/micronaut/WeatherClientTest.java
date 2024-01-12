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
import example.openmeteo.api.WeatherForecastApisApi;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

// tag::test[]
@MicronautTest
class WeatherClientTest {
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
}
// end::test[]
