from dataclasses import dataclass

from micronaut.core.annotation import ReflectiveAccess
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@ReflectiveAccess  # <2>
@dataclass
class Photo:
    albumId: int
    title: str
    url: str
    thumbnailUrl: str
