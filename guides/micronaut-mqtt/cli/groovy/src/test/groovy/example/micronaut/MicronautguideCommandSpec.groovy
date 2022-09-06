package example.micronaut

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import spock.lang.Specification
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8
import static org.awaitility.Awaitility.await;

class MicronautguideCommandSpec extends Specification {

    def "test with command line option"() {
        when:
        OutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))

        ApplicationContext ctx = ApplicationContext.run(
                ['spec.name': 'MicronautguideCommandSpec'],
                Environment.CLI, Environment.TEST)

        String[] args = ['-t', '212', '-s', 'Fahrenheit']
        PicocliRunner.run(MicronautguideCommand, ctx, args)

        TemperatureListener listener = ctx.getBean(TemperatureListener.class);

        then:
        baos.toString().contains('Topic published')

        and:
        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertEquals(new BigDecimal("100.00"), listener.temperature));

        cleanup:
        ctx.close()
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandTest")
    @MqttSubscriber // <1>
    static class TemperatureListener {

        private final Logger LOG = LoggerFactory.getLogger(TemperatureListener.class)
        private BigDecimal temperature

        @Topic("house/livingroom/temperature") // <2>
        void receive(byte[] data) {
            temperature = new BigDecimal(new String(data, UTF_8))
        }
    }
}
