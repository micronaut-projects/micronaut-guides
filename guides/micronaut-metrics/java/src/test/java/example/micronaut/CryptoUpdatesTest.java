package example.micronaut;

import example.micronaut.crypto.CryptoService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import io.micronaut.core.util.CollectionUtils;
import java.util.Collections;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.micronaut.core.util.StringUtils;

public class CryptoUpdatesTest {

    public static EmbeddedServer embeddedServer;

    public static EmbeddedServer kucoinEmbeddedServer;

    @BeforeAll
    public static void beforeAll() {
        kucoinEmbeddedServer = ApplicationContext.run(EmbeddedServer.class,
                CollectionUtils.mapOf(
                        "spec.name", "MetricsTestKucoin")); // <1>
        embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                CollectionUtils.mapOf(
                        "crypto.initial-delay", "10h", // <2>
                        "micronaut.http.services.kucoin.url", "http://localhost:" + kucoinEmbeddedServer.getPort(), // <3>
                        "app.scheduled.enabled", StringUtils.TRUE));
    }

    @AfterAll
    public static void afterAll() {
        embeddedServer.close();
        kucoinEmbeddedServer.close();
    }

    @Test
    void testCryptoUpdates() {
        CryptoService cryptoService = embeddedServer.getApplicationContext().getBean(CryptoService.class);
        MeterRegistry meterRegistry = embeddedServer.getApplicationContext().getBean(MeterRegistry.class);

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

    @Requires(property = "spec.name", value = "MetricsTestKucoin") // <1>
    @Controller
    static class MockKucoinController {
        @Get("/api/v1/market/orderbook/level1")
        String latest(@QueryValue String symbol) {
            return "{\"code\":\"200000\",\"data\":{\"time\":1654865889872,\"sequence\":\"1630823934334\",\"price\":\"29670.4\",\"size\":\"0.00008436\",\"bestBid\":\"29666.4\",\"bestBidSize\":\"0.16848947\",\"bestAsk\":\"29666.5\",\"bestAskSize\":\"2.37840044\"}}";
        }
    }
}
