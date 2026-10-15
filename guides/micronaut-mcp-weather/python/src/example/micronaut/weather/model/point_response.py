from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from example.micronaut.weather.model.point_properties import PointProperties


@Serdeable
@dataclass
class PointResponse:
    properties: PointProperties | None = None
