from dataclasses import dataclass

from micronaut.core.annotation import Introspected


@dataclass
@Introspected  # <1>
class ContactComplete:
    id: int | None
    firstName: str
    lastName: str
    phones: list[str] | None = None
