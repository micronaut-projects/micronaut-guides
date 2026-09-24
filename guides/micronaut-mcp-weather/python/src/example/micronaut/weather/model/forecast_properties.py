from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from example.micronaut.weather.model.period import Period


@Serdeable
@dataclass
class ForecastProperties:
    periods: list[Period] | None = None
