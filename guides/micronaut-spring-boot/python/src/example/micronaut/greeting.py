from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class Greeting:
    id: int
    content: str
