package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.env.Environment;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MicronautguideCommandTest {

    @Test
    void testWithCommandLineOption() {
        OutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(
                CollectionUtils.mapOf(
                        "mqtt.enabled", false,
                        "spec.name", "MicronautguideCommandTest"),
                Environment.CLI, Environment.TEST)) {

            String[] args = new String[] { "-t", "212", "-s", "Fahrenheit" };
            PicocliRunner.run(MicronautguideCommand.class, ctx, args);
            assertTrue(baos.toString().contains("Topic published"));

            List<BigDecimal> temperatures = ctx.getBean(TemperatureClientReplacement.class).getTemperatures();
            assertEquals(Collections.singletonList(new BigDecimal("100.00")), temperatures);
        }
    }

    @Replaces(TemperatureClient.class)
    @Singleton
    static class TemperatureClientReplacement implements TemperatureClient {

        private final List<byte[]> temperatures = new ArrayList<>();

        @Override
        public void publishLivingroomTemperature(byte[] data) {
            temperatures.add(data);
        }

        List<BigDecimal> getTemperatures() {
            return temperatures.stream()
                    .map(bytes -> new BigDecimal(new String(bytes, UTF_8)))
                    .collect(Collectors.toList());
        }
    }
}
