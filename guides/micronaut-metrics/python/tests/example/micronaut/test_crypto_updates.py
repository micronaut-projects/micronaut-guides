from typing import Annotated

import java
import pytest
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, QueryValue
from pyronaut.test import MicronautTest, micronaut_test_fixture

EmbeddedServer = java.type("io.micronaut.runtime.server.EmbeddedServer")
MeterRegistry = java.type("io.micrometer.core.instrument.MeterRegistry")
TimeUnit = java.type("java.util.concurrent.TimeUnit")


@Requires(property="spec.name", value="MetricsTestKucoin")
@Controller
class MockKucoinController:
    RESPONSE = """
        {
        "code":"200000",
           "data":{
              "time":1654865889872,
              "sequence":"1630823934334",
              "price":"29670.4",
              "size":"0.00008436",
              "bestBid":"29666.4",
              "bestBidSize":"0.16848947",
              "bestAsk":"29666.5",
              "bestAskSize":"2.37840044"
           }
        }"""

    @Get("/api/v1/market/orderbook/level1")
    def latest(self, symbol: Annotated[str, QueryValue]) -> str:
        return self.RESPONSE


@pytest.fixture
def kucoin_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"spec.name": "MetricsTestKucoin"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def my_context(request, kucoin_context):
    kucoin_server = kucoin_context[EmbeddedServer]
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.kucoin.url": f"http://localhost:{kucoin_server.getPort()}",
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def crypto_service(my_context):
    return my_context["example.micronaut.crypto.CryptoService"]


@pytest.fixture
def meter_registry(my_context):
    return my_context[MeterRegistry]


def test_crypto_updates(crypto_service, meter_registry):
    counter = meter_registry.counter("bitcoin.price.checks")
    timer = meter_registry.timer("bitcoin.price.time")

    assert counter.count() == pytest.approx(0)
    assert timer.totalTime(TimeUnit.MILLISECONDS) == pytest.approx(0)

    checks = 3
    for _ in range(checks):
        crypto_service.update_price()

    assert counter.count() == pytest.approx(checks)
    assert timer.totalTime(TimeUnit.MILLISECONDS) > 0
