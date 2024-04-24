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
package example.micronaut

import example.micronaut.Scale.CELSIUS
import example.micronaut.Scale.FAHRENHEIT
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.math.BigDecimal
import java.math.RoundingMode

@Command(name = "house-livingroom-temperature", // <1>
         description = ["Publish living room temperature"],
         mixinStandardHelpOptions = true)
class MicronautguideCommand : Runnable {

    @Option(names = ["-t", "--temperature"], // <2>
            required = true, // <3>
            description = ["Temperature value"])
    var temperature: BigDecimal? = null

    @Option(names = ["-s", "--scale"], // <2>
            required = false, // <3>
            description = ["Temperate scales \${COMPLETION-CANDIDATES}"], // <4>
            completionCandidates = TemperatureScaleCandidates::class) // <4>
    var scale: String? = null

    @Inject
    lateinit var temperatureClient: TemperatureClient // <5>

    override fun run() {
        val temperatureScale = if (scale != null) Scale.of(scale!!).orElse(CELSIUS) else CELSIUS
        val celsius = if (temperatureScale === FAHRENHEIT) fahrenheitToCelsius(temperature!!) else temperature!!
        val data = celsius.toPlainString().toByteArray()
        temperatureClient.publishLivingroomTemperature(data) // <6>
        println("Topic published")
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(MicronautguideCommand::class.java, *args)
        }

        private fun fahrenheitToCelsius(temperature: BigDecimal): BigDecimal {
            return temperature
                    .subtract(BigDecimal.valueOf(32))
                    .multiply(BigDecimal.valueOf(5 / 9.0))
                    .setScale(2, RoundingMode.FLOOR)
        }
    }
}
