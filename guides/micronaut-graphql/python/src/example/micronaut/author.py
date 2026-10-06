from dataclasses import dataclass

from micronaut.core.annotation import Introspected


@Introspected
@dataclass
class Author:
    id: str
    first_name: str
    last_name: str
