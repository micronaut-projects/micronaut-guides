from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class Period:
    name: str | None = None
    temperature: int = 0
    temperatureUnit: str | None = None
    windSpeed: str | None = None
    windDirection: str | None = None
    detailedForecast: str | None = None
