package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Command(name = "cli", description = "...",
        mixinStandardHelpOptions = true)
public class CliCommand implements Runnable {

    @Option(names = {"-t", "--temperature"},
            required = true,
            description = "Temperature")
    BigDecimal temperature;

    @Option(names = {"-s", "--scale"},
            required = false,
            description = "Temperate scales ${COMPLETION-CANDIDATES}",
            completionCandidates = TemperatureScaleCandidates.class)
    String scale;

    @Inject
    TemperatureClient temperatureClient;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(CliCommand.class, args);
    }

    public void run() {
        Scale temperatureScale = scale != null ? Scale.of(scale).orElse(Scale.CELSIUS) : Scale.CELSIUS;
        BigDecimal celsius = (temperatureScale == Scale.FAHRENHEIT) ? fahrenheitToCelsius(temperature) : temperature;
        System.out.println("Temperature " + celsius + "º");
        temperatureClient.publishLivingroomTemperature(celsius.toPlainString().getBytes());
    }

    public static BigDecimal fahrenheitToCelsius(BigDecimal temperature) {
        return (temperature.subtract(BigDecimal.valueOf(32))).multiply(BigDecimal.valueOf(5/9.0)).setScale(2, RoundingMode.FLOOR);
    }
}
