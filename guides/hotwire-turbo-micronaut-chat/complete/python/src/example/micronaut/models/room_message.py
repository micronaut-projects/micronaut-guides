from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from java.time import Instant, ZoneId
from java.time.format import DateTimeFormatter
from micronaut.serde.annotation import Serdeable


# tag::clazz[]
@dataclass
@Serdeable  # <1>
class RoomMessage:
    id: Annotated[int, NotNull]
    room: Annotated[int, NotNull]
    content: Annotated[str, NotBlank]
    dateCreated: Instant

    def formattedDateCreated(self) -> str:
        formatter = DateTimeFormatter.ofPattern("MMM:dd HH:mm:ss").withZone(
            ZoneId.systemDefault()
        )
        return formatter.format(self.dateCreated)
# end::clazz[]
