from dataclasses import dataclass

from micronaut.jsonschema import JsonSchema
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
@JsonSchema  # <2>
class GetAlertInput:
    """Input for the getAlerts tool."""

    state: str
