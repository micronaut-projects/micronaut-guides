from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from example.micronaut.weather.model.properties import Properties


@Serdeable
@dataclass
class Feature:
    id: str | None = None
    type: str | None = None
    geometry: object | None = None
    properties: Properties | None = None
