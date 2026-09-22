from typing import Annotated

import java
from jakarta.inject import Inject, Singleton
from micronaut.scheduling.annotation import Scheduled

from .price_client import PriceClient

AtomicInteger = java.type("java.util.concurrent.atomic.AtomicInteger")
LoggerFactory = java.type("org.slf4j.LoggerFactory")
MeterRegistry = java.type("io.micrometer.core.instrument.MeterRegistry")
Timer = java.type("io.micrometer.core.instrument.Timer")


@Singleton  # <1>
class CryptoService:
    def __init__(
        self,
        price_client: Annotated[PriceClient, Inject],  # <2>
        meter_registry: Annotated[MeterRegistry, Inject],
    ):
        self.log = LoggerFactory.getLogger("example.micronaut.crypto.CryptoService")
        self.price_client = price_client
        self.checks = meter_registry.counter("bitcoin.price.checks")  # <3>
        self.time = meter_registry.timer("bitcoin.price.time")  # <4>
        self.latest_price_usd = AtomicInteger(0)
        meter_registry.gauge("bitcoin.price.latest", self.latest_price_usd)  # <5>

    @Scheduled(
        fixedRate="${crypto.update-frequency:1h}",
        initialDelay="${crypto.initial-delay:0s}",
    )  # <6>
    def update_price(self) -> None:
        sample = Timer.start()  # <7>
        try:
            self.checks.increment()  # <8>
            self.latest_price_usd.set(int(self.price_client.latest_in_usd().price))  # <9>
        except Exception as exc:
            self.log.error(f"Problem checking price: {exc}")
        finally:
            sample.stop(self.time)
