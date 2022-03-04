package example.micronaut

import example.micronaut.Scale.CELSIUS
import example.micronaut.Scale.FAHRENHEIT
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
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
        LOG.info("Topic published")
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(MicronautguideCommand::class.java)

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
