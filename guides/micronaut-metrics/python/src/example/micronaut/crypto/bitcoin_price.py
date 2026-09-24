from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable  # <1>
class Data:
    price: float


@dataclass
@Serdeable  # <1>
class BitcoinPrice:
    data: Data

    @property
    def price(self) -> float:
        return self.data.price
