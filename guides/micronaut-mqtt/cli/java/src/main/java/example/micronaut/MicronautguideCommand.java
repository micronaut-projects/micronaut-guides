/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static example.micronaut.Scale.CELSIUS;
import static example.micronaut.Scale.FAHRENHEIT;

@Command(name = "house-livingroom-temperature", // <1>
         description = "Publish living room temperature",
         mixinStandardHelpOptions = true)
public class MicronautguideCommand implements Runnable {

    @Option(names = {"-t", "--temperature"}, // <2>
            required = true, // <3>
            description = "Temperature value")
    BigDecimal temperature;

    @Option(names = {"-s", "--scale"}, // <2>
            required = false, // <3>
            description = "Temperate scales ${COMPLETION-CANDIDATES}", // <4>
            completionCandidates = TemperatureScaleCandidates.class) // <4>
    String scale;

    @Inject
    TemperatureClient temperatureClient; // <5>

    public static void main(String[] args) {
        PicocliRunner.run(MicronautguideCommand.class, args);
    }

    public void run() {
        Scale temperatureScale = scale != null ?
                Scale.of(scale).orElse(CELSIUS) : CELSIUS;
        BigDecimal celsius = (temperatureScale == FAHRENHEIT)
                ? fahrenheitToCelsius(temperature) : temperature;
        byte[] data = celsius.toPlainString().getBytes();
        temperatureClient.publishLivingroomTemperature(data); // <6>
        System.out.println("Topic published");
    }

    private static BigDecimal fahrenheitToCelsius(BigDecimal temperature) {
        return temperature
                .subtract(BigDecimal.valueOf(32))
                .multiply(BigDecimal.valueOf(5/9.0))
                .setScale(2, RoundingMode.FLOOR);
    }
}
