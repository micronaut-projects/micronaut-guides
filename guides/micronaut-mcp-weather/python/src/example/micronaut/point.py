from dataclasses import dataclass

from micronaut.jsonschema import JsonSchema
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
@JsonSchema  # <2>
class Point:
    """Input for the getWeatherForecastByLocation tool."""

    latitude: float
    longitude: float
