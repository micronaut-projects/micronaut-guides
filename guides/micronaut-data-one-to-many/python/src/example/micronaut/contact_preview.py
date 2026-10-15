from dataclasses import dataclass

from micronaut.core.annotation import Introspected


@dataclass
@Introspected  # <1>
class ContactPreview:
    id: int | None
    firstName: str
    lastName: str
