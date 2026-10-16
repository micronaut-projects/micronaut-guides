from enum import Enum

from com.fasterxml.jackson.annotation import JsonValue
from micronaut.serde.annotation import Serdeable


@Serdeable
class Player(Enum):
    WHITE = "w"
    BLACK = "b"

    @JsonValue
    def to_string(self) -> str:
        return self.value
