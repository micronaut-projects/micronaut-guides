package example.micronaut

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.logging.LoggingSystem
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static io.micronaut.logging.LogLevel.ALL

@MicronautTest // <1>
class MetricsSpec extends Specification {

    @Inject
    MeterRegistry meterRegistry // <2>

    @Inject
    LoggingSystem loggingSystem // <3>

    @Inject
    @Client('/')
    HttpClient httpClient // <4>

    void testExpectedMeters() {

        when:
        Set<String> names = meterRegistry.meters.id.name

        then:
        // check that a subset of expected meters exist
        names.contains 'jvm.memory.max'
        names.contains 'process.uptime'
        names.contains 'system.cpu.usage'
        names.contains 'process.files.open'
        names.contains 'logback.events'
        names.contains 'hikaricp.connections.max'

        // these will be lazily created
        !names.contains('http.client.requests')
        !names.contains('http.server.requests')
    }

    void testHttp() {

        when:
        Timer timer = meterRegistry.timer('http.server.requests', Tags.of(
                'exception', 'none',
                'method', 'GET',
                'status', '200',
                'uri', '/books'))

        then:
        0 == timer.count()

        when:
        httpClient.toBlocking().retrieve(
                HttpRequest.GET('/books'),
                Argument.listOf(Book))

        then:
        1 == timer.count()
    }

    void testLogback() {

        when:
        Counter counter = meterRegistry.counter('logback.events', Tags.of('level', 'info'))
        double initial = counter.count()

        Logger logger = LoggerFactory.getLogger('testing.testing')
        loggingSystem.setLogLevel('testing.testing', ALL)

        logger.trace('trace')
        logger.debug('debug')
        logger.info('info')
        logger.warn('warn')
        logger.error('error')

        then:
        initial + 1 == counter.count()
    }

    void testMetricsEndpoint() {

        when:
        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET('/metrics'),
                Argument.mapOf(String, Object))

        then:
        response.containsKey 'names'
        response.get('names') in List

        when:
        List<String> names = (List<String>) response.get('names')

        then:
        // check that a subset of expected meters exist
        names.contains 'jvm.memory.max'
        names.contains 'process.uptime'
        names.contains 'system.cpu.usage'
        names.contains 'process.files.open'
        names.contains 'logback.events'
        names.contains 'hikaricp.connections.max'
    }

    void testOneMetricEndpoint() {

        when:
        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET('/metrics/jvm.memory.used'),
                Argument.mapOf(String, Object))

        String name = (String) response.get('name')

        then:
        'jvm.memory.used' == name

        when:
        List<Map<String, Object>> measurements = (List<Map<String, Object>>) response.get('measurements')

        then:
        1 == measurements.size()

        when:
        double value = (double) measurements.get(0).get('value')

        then:
        value > 0
    }
}
