from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

@Serdeable  # <1>
@dataclass
class Photo:
    albumId: int
    title: str
    url: str
    thumbnailUrl: str
