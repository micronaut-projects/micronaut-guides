from dataclasses import dataclass

from micronaut.core.annotation import ReflectiveAccess
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@ReflectiveAccess
@dataclass
class Photo:
    id: int
    title: str
    url: str
    thumbnailUrl: str
