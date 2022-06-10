package example.micronaut

import example.micronaut.crypto.CryptoService
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.*
import java.util.*
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CryptoUpdatesTest {
    lateinit var embeddedServer: EmbeddedServer
    lateinit var kucoinEmbeddedServer: EmbeddedServer
    @BeforeAll
    fun beforeAll() {
        kucoinEmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java,
                Collections.singletonMap<String, Any>("spec.name", "MetricsTestKucoin"))
        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java,
                Collections.singletonMap<String, Any>("micronaut.http.services.kucoin.url", "http://localhost:" + kucoinEmbeddedServer.getPort()))
    }

    @AfterAll
    fun afterAll() {
        embeddedServer.close()
        kucoinEmbeddedServer.close()
    }

    @Test
    fun testCryptoUpdates() {
        val cryptoService = embeddedServer.applicationContext.getBean(CryptoService::class.java)
        val meterRegistry = embeddedServer.applicationContext.getBean(MeterRegistry::class.java)
        val counter = meterRegistry.counter("bitcoin.price.checks")
        val timer = meterRegistry.timer("bitcoin.price.time")

        Assertions.assertEquals(0.0, counter.count(), 0.000001)
        Assertions.assertEquals(0.0, timer.totalTime(TimeUnit.MILLISECONDS))

        val checks = 3

        for (i in 0 until checks) {
            cryptoService.updatePrice()
        }

        Assertions.assertEquals(checks.toDouble(), counter.count(), 0.000001)
        Assertions.assertTrue(timer.totalTime(TimeUnit.MILLISECONDS) > 0)
    }

    @Requires(property = "spec.name", value = "MetricsTestKucoin")
    @Controller
    internal class MockKucoinController {
        @Get("/api/v1/market/orderbook/level1")
        fun latest(@QueryValue symbol: String?): String {
            return "{\"code\":\"200000\",\"data\":{\"time\":1654865889872,\"sequence\":\"1630823934334\",\"price\":\"29670.4\",\"size\":\"0.00008436\",\"bestBid\":\"29666.4\",\"bestBidSize\":\"0.16848947\",\"bestAsk\":\"29666.5\",\"bestAskSize\":\"2.37840044\"}}"
        }
    }
}