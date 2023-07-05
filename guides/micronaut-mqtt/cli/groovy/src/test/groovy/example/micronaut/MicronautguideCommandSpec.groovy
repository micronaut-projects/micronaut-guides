package example.micronaut

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static java.nio.charset.StandardCharsets.UTF_8

class MicronautguideCommandSpec extends Specification {

    PollingConditions pollingConditions = new PollingConditions()

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
        pollingConditions.within(5) {
            listener.temperature == 100.0
        }

        cleanup:
        ctx.close()
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandSpec")
    @MqttSubscriber // <1>
    static class TemperatureListener {

        private BigDecimal temperature

        @Topic("house/livingroom/temperature") // <2>
        void receive(byte[] data) {
            temperature = new BigDecimal(new String(data, UTF_8))
        }
    }
}
