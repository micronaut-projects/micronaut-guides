package example.micronaut

import example.micronaut.crypto.CryptoService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static java.util.concurrent.TimeUnit.MILLISECONDS

class CryptoUpdatesSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer kucoinEmbeddedServer = ApplicationContext.run(EmbeddedServer.class,
            ["spec.name": "MetricsTestKucoin"])

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class,
            ["micronaut.http.services.kocoin.url": "http://localhost:${kucoinEmbeddedServer.port}"])

    void "test crypto updates"() {
        given:
        CryptoService cryptoService = embeddedServer.getApplicationContext().getBean(CryptoService.class);
        MeterRegistry meterRegistry = embeddedServer.getApplicationContext().getBean(MeterRegistry.class);

        when:
        Counter counter = meterRegistry.counter("bitcoin.price.checks");
        Timer timer = meterRegistry.timer("bitcoin.price.time");

        then:
        0 == counter.count()
        0 == timer.totalTime(MILLISECONDS)

        when:
        int checks = 3;
        for (int i = 0; i < checks; i++) {
            cryptoService.updatePrice();
        }

        then:
        checks == counter.count()
        timer.totalTime(MILLISECONDS) > 0
    }

    @Requires(property = "spec.name", value = "MetricsTestKucoin")
    @Controller
    static class MockKucoinController {
        @Get("/api/v1/market/orderbook/level1")
        String latest(@QueryValue String symbol) {
            '{"code":"200000","data":{"time":1654865889872,"sequence":"1630823934334","price":"29670.4","size":"0.00008436","bestBid":"29666.4","bestBidSize":"0.16848947","bestAsk":"29666.5","bestAskSize":"2.37840044"}}'
        }
    }
}
