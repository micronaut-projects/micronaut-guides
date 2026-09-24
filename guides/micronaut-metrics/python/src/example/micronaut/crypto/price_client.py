from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.http.annotation import Get, QueryValue
from micronaut.http.client.annotation import Client

from .bitcoin_price import BitcoinPrice


@Client(id="kucoin")  # <1>
class PriceClient(ABC):

    @Get("/api/v1/market/orderbook/level1")
    @abstractmethod
    def latest(self, symbol: Annotated[str, QueryValue]) -> BitcoinPrice:
        ...

    def latest_in_usd(self) -> BitcoinPrice:
        return self.latest("BTC-USDT")
