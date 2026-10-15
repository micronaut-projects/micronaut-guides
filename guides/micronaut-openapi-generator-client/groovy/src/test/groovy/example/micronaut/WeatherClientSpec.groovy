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
package example.micronaut

import example.openmeteo.api.WeatherForecastApisApi
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

// tag::test[]
@MicronautTest
class WeatherClientSpec extends Specification {

    @Inject
    WeatherForecastApisApi api // <1>

    void "fetches weather for Montaigu-Vendee"() {
        when:
        def forecast = api.v1ForecastGet(46.97386f, -1.3111076f, // <2>
                null,
                null,
                true,
                null,
                null,
                null,
                null,
                null)
        def weather = forecast.block().currentWeather // <3>

        then:
        weather.temperature < 50
    }
}
// end::test[]
