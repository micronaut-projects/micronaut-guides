from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class Properties:
    id: str | None = None
    areaDesc: str | None = None
    event: str | None = None
    severity: str | None = None
    description: str | None = None
    instruction: str | None = None
