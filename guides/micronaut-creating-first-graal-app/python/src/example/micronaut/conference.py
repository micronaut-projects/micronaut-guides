from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class Conference:
    name: str
