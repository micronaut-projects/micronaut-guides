from dataclasses import dataclass
from typing import Annotated

from micronaut.context.annotation import EachProperty, Parameter


@EachProperty("stadium")  # <1>
@dataclass(init=False)
class StadiumConfiguration:
    name: str  # <2>
    city: str | None
    size: int | None

    def __init__(
        self,
        name: Annotated[str, Parameter],
        city: str | None = None,
        size: int | None = None,
    ):
        self.name = name
        self.city = city
        self.size = size
