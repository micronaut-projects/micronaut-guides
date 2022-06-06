package example.micronaut;

import example.micronaut.crypto.CryptoService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.logging.LoggingSystem;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.micronaut.logging.LogLevel.ALL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class MetricsTest {

    @Inject
    MeterRegistry meterRegistry; // <2>

    @Inject
    LoggingSystem loggingSystem; // <3>

    @Inject
    CryptoService cryptoService;

    @Inject
    @Client("/")
    HttpClient httpClient; // <4>

    @Test
    void testExpectedMeters() {

        Set<String> names = meterRegistry.getMeters().stream()
                .map(meter -> meter.getId().getName())
                .collect(Collectors.toSet());

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"));
        assertTrue(names.contains("process.uptime"));
        assertTrue(names.contains("system.cpu.usage"));
        assertTrue(names.contains("process.files.open"));
        assertTrue(names.contains("logback.events"));
        assertTrue(names.contains("hikaricp.connections.max"));

        // these will be lazily created
        assertFalse(names.contains("http.client.requests"));
        assertFalse(names.contains("http.server.requests"));
    }

    @Test
    void testHttp() {

        Timer timer = meterRegistry.timer("http.server.requests", Tags.of(
                "exception", "none",
                "method", "GET",
                "status", "200",
                "uri", "/books"));
        assertEquals(0, timer.count());

        httpClient.toBlocking().retrieve(
                HttpRequest.GET("/books"),
                Argument.listOf(Book.class));

        assertEquals(1, timer.count());
    }

    @Test
    void testLogback() {

        Counter counter = meterRegistry.counter("logback.events", Tags.of("level", "info"));
        double initial = counter.count();

        Logger logger = LoggerFactory.getLogger("testing.testing");
        loggingSystem.setLogLevel("testing.testing", ALL);

        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");

        assertEquals(initial + 1, counter.count(), 0.000001);
    }

    @Test
    void testMetricsEndpoint() {

        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET("/metrics"),
                Argument.mapOf(String.class, Object.class));

        assertTrue(response.containsKey("names"));
        assertTrue(response.get("names") instanceof List);

        List<String> names = (List<String>) response.get("names");

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"));
        assertTrue(names.contains("process.uptime"));
        assertTrue(names.contains("system.cpu.usage"));
        assertTrue(names.contains("process.files.open"));
        assertTrue(names.contains("logback.events"));
        assertTrue(names.contains("hikaricp.connections.max"));
    }

    @Test
    void testOneMetricEndpoint() {

        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET("/metrics/jvm.memory.used"),
                Argument.mapOf(String.class, Object.class));

        String name = (String) response.get("name");
        assertEquals("jvm.memory.used", name);

        List<Map<String, Object>> measurements = (List<Map<String, Object>>) response.get("measurements");
        assertEquals(1, measurements.size());

        double value = (double) measurements.get(0).get("value");
        assertTrue(value > 0);
    }

    @Test
    void testCryptoUpdates() {

        Counter counter = meterRegistry.counter("bitcoin.price.checks");
        Timer timer = meterRegistry.timer("bitcoin.price.time");

        assertEquals(0, counter.count(), 0.000001);
        assertEquals(0, timer.totalTime(MILLISECONDS));

        int checks = 3;

        for (int i = 0; i < checks; i++) {
            cryptoService.updatePrice();
        }

        assertEquals(checks, counter.count(), 0.000001);
        assertTrue(timer.totalTime(MILLISECONDS) > 0);
    }
}
