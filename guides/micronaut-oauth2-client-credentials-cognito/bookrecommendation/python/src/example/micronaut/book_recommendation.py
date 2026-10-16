from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass(frozen=True)
class BookRecommendation:
    name: str
