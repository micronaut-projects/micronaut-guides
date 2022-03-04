package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Command(name = "house-livingroom-temperature",  // <1>
        description = "Publish living room temperature",
        mixinStandardHelpOptions = true)
public class MicronautguideCommand implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MicronautguideCommand.class);

    @Option(names = {"-t", "--temperature"}, // <2>
            required = true, // <3>
            description = "Temperature value")
    BigDecimal temperature;

    @Option(names = {"-s", "--scale"}, // <2>
            required = false, // <3>
            description = "Temperate scales ${COMPLETION-CANDIDATES}",  // <4>
            completionCandidates = TemperatureScaleCandidates.class)  // <4>
    String scale;

    @Inject
    TemperatureClient temperatureClient;  // <5>

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(MicronautguideCommand.class, args);
    }

    public void run() {
        Scale temperatureScale = scale != null ?
                Scale.of(scale).orElse(Scale.CELSIUS) : Scale.CELSIUS;
        BigDecimal celsius = (temperatureScale == Scale.FAHRENHEIT)
                ? fahrenheitToCelsius(temperature) : temperature;
        byte[] data = celsius.toPlainString().getBytes();
        temperatureClient.publishLivingroomTemperature(data); // <6>
        System.out.println("Topic published");
    }

    private static BigDecimal fahrenheitToCelsius(BigDecimal temperature) {
        return (temperature.subtract(BigDecimal.valueOf(32)))
                .multiply(BigDecimal.valueOf(5/9.0)).setScale(2, RoundingMode.FLOOR);
    }
}
