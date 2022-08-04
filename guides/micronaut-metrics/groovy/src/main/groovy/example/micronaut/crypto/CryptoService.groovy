package example.micronaut.crypto

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton

import java.util.concurrent.atomic.AtomicInteger

@CompileStatic
@Slf4j
@Singleton // <1>
class CryptoService {

    private final PriceClient priceClient
    private final Counter checks
    private final Timer time
    private final AtomicInteger latestPriceUsd = new AtomicInteger(0)

    CryptoService(PriceClient priceClient, // <2>
                  MeterRegistry meterRegistry) {
        this.priceClient = priceClient

        checks = meterRegistry.counter('bitcoin.price.checks') // <3>
        time = meterRegistry.timer('bitcoin.price.time') // <4>
        meterRegistry.gauge('bitcoin.price.latest', latestPriceUsd) // <5>
    }

    @Scheduled(fixedRate = '${crypto.updateFrequency:1h}',
            initialDelay = '${crypto.initialDelay:0s}') // <6>
    void updatePrice() {
        time.record(() -> { // <7>
            try {
                checks.increment() // <8>
                latestPriceUsd.set((int) priceClient.latestInUSD().price) // <9>
            } catch (Exception e) {
                log.error('Problem checking price', e)
            }
        })
    }
}
