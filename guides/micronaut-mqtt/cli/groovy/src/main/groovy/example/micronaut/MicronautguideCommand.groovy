package example.micronaut

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

import java.math.RoundingMode

import static example.micronaut.Scale.CELSIUS
import static example.micronaut.Scale.FAHRENHEIT

@Slf4j
@CompileStatic
@Command(name = 'house-livingroom-temperature', // <1>
         description = 'Publish living room temperature',
         mixinStandardHelpOptions = true)
class MicronautguideCommand implements Runnable {

    @Option(names = ['-t', '--temperature'], // <2>
            required = true, // <3>
            description = 'Temperature value')
    BigDecimal temperature

    @Option(names = ['-s', '--scale'], // <2>
            required = false, // <3>
            description = 'Temperate scales ${COMPLETION-CANDIDATES}', // <4>
            completionCandidates = TemperatureScaleCandidates) // <4>
    String scale

    @Inject
    TemperatureClient temperatureClient // <5>

    static void main(String[] args) {
        PicocliRunner.run(MicronautguideCommand, args)
    }

    void run() {
        Scale temperatureScale = scale != null ?
                Scale.of(scale).orElse(CELSIUS) : CELSIUS
        BigDecimal celsius = (temperatureScale == FAHRENHEIT)
                ? fahrenheitToCelsius(temperature) : temperature
        byte[] data = celsius.toPlainString().bytes
        temperatureClient.publishLivingroomTemperature(data) // <6>
        log.info('Topic published')
    }

    private static BigDecimal fahrenheitToCelsius(BigDecimal temperature) {
        return temperature
                .subtract(BigDecimal.valueOf(32))
                .multiply(BigDecimal.valueOf(5/9.0 as double))
                .setScale(2, RoundingMode.FLOOR)
    }
}
