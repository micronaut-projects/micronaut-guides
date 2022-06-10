package example.micronaut

import example.micronaut.crypto.CryptoService
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.logging.LogLevel.ALL
import io.micronaut.logging.LoggingSystem
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit.MILLISECONDS

@MicronautTest // <1>
class MetricsTest {

    @Inject
    lateinit var meterRegistry: MeterRegistry // <2>

    @Inject
    lateinit var loggingSystem: LoggingSystem // <3>

    @Inject
    lateinit var cryptoService: CryptoService

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <4>

    @Test
    fun testExpectedMeters() {

        val names = meterRegistry.meters.map { it.id.name }

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"))
        assertTrue(names.contains("process.uptime"))
        assertTrue(names.contains("system.cpu.usage"))
        assertTrue(names.contains("process.files.open"))
        assertTrue(names.contains("logback.events"))
        assertTrue(names.contains("hikaricp.connections.max"))

        // these will be lazily created
        assertFalse(names.contains("http.client.requests"))
        assertFalse(names.contains("http.server.requests"))
    }

    @Test
    fun testHttp() {

        val timer = meterRegistry.timer(
            "http.server.requests", Tags.of(
                "exception", "none",
                "method", "GET",
                "status", "200",
                "uri", "/books"))

        assertEquals(0, timer.count())

        httpClient.toBlocking().retrieve(
            HttpRequest.GET<Any>("/books"),
            Argument.listOf(Book::class.java))

        assertEquals(1, timer.count())
    }

    @Test
    fun testLogback() {

        val counter = meterRegistry.counter("logback.events", Tags.of("level", "info"))
        val initial = counter.count()

        val logger = LoggerFactory.getLogger("testing.testing")
        loggingSystem.setLogLevel("testing.testing", ALL)

        logger.trace("trace")
        logger.debug("debug")
        logger.info("info")
        logger.warn("warn")
        logger.error("error")

        assertEquals(initial + 1, counter.count(), 0.000001)
    }

    @Test
    fun testMetricsEndpoint() {

        val response = httpClient.toBlocking().retrieve(
            HttpRequest.GET<Any>("/metrics"),
            Argument.mapOf(String::class.java, Any::class.java)
        )

        assertTrue(response.containsKey("names"))
        assertTrue(response["names"] is List<*>)

        val names = response["names"] as List<String>

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"))
        assertTrue(names.contains("process.uptime"))
        assertTrue(names.contains("system.cpu.usage"))
        assertTrue(names.contains("process.files.open"))
        assertTrue(names.contains("logback.events"))
        assertTrue(names.contains("hikaricp.connections.max"))
    }

    @Test
    fun testOneMetricEndpoint() {

        val response = httpClient.toBlocking().retrieve(
            HttpRequest.GET<Any>("/metrics/jvm.memory.used"),
            Argument.mapOf(String::class.java, Any::class.java))

        val name = response["name"] as String
        assertEquals("jvm.memory.used", name)

        val measurements = response["measurements"] as List<Map<String, Any>>
        assertEquals(1, measurements.size)

        val value = measurements[0]["value"] as Double
        assertTrue(value > 0)
    }
}
