from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from example.micronaut.weather.model.feature import Feature


@Serdeable
@dataclass
class Alerts:
    context: list[str] | None = None
    type: str | None = None
    features: list[Feature] | None = None
    title: str | None = None
    updated: str | None = None
