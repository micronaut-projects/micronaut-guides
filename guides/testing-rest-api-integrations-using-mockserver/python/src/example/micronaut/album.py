from dataclasses import dataclass

from micronaut.core.annotation import ReflectiveAccess
from micronaut.serde.annotation import Serdeable

from .photo import Photo


@Serdeable  # <1>
@ReflectiveAccess
@dataclass
class Album:
    albumId: int
    photos: list[Photo]
