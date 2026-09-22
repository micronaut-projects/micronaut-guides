from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from example.micronaut.weather.model.forecast_properties import ForecastProperties


@Serdeable
@dataclass
class Forecast:
    properties: ForecastProperties | None = None
