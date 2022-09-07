package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.awaitility.Awaitility.await;

class MicronautguideCommandTest {

    @Test
    void testWithCommandLineOption() {
        OutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(
                CollectionUtils.mapOf("spec.name", "MicronautguideCommandTest"),
                Environment.CLI, Environment.TEST)) {

            String[] args = new String[] { "-t", "212", "-s", "Fahrenheit" };
            PicocliRunner.run(MicronautguideCommand.class, ctx, args);
            assertTrue(baos.toString().contains("Topic published"));

            TemperatureListener listener = ctx.getBean(TemperatureListener.class);
            await().atMost(5, TimeUnit.SECONDS)
                    .untilAsserted(() -> assertEquals(new BigDecimal("100.00"), listener.temperature));
        }
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandTest")
    @MqttSubscriber // <1>
    public static class TemperatureListener {

        private BigDecimal temperature;

        @Topic("house/livingroom/temperature") // <2>
        public void receive(byte[] data) {
            temperature = new BigDecimal(new String(data, UTF_8));
        }
    }
}
