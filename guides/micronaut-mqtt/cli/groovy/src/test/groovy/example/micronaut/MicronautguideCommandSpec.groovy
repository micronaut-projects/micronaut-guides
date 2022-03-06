package example.micronaut

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton
import spock.lang.Specification

import static java.nio.charset.StandardCharsets.UTF_8

class MicronautguideCommandSpec extends Specification {

    void testWithCommandLineOption() {
        when:
        OutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))

        ApplicationContext ctx = ApplicationContext.run(
                ['mqtt.enabled': false, 'spec.name': 'MicronautguideCommandSpec'],
                Environment.CLI, Environment.TEST)

        String[] args = ['-t', '212', '-s', 'Fahrenheit']
        PicocliRunner.run(MicronautguideCommand, ctx, args)

        then:
        baos.toString().contains('Topic published')

        when:
        List<BigDecimal> temperatures = ctx.getBean(TemperatureClientReplacement).temperatures

        then:
        [new BigDecimal('100.00')] == temperatures

        cleanup:
        ctx.close()
    }

    @Requires(property = 'spec.name', value = 'MicronautguideCommandSpec')
    @Replaces(TemperatureClient)
    @Singleton
    static class TemperatureClientReplacement implements TemperatureClient {

        private final List<byte[]> temperatures = []

        @Override
        void publishLivingroomTemperature(byte[] data) {
            temperatures << data
        }

        List<BigDecimal> getTemperatures() {
            temperatures.collect { new BigDecimal(new String(it, UTF_8)) }
        }
    }
}
