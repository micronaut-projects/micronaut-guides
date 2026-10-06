from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class News:
    month: str
    headlines: list[str]
