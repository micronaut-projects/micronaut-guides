from dataclasses import dataclass
from typing import Annotated

from micronaut.context.annotation import EachProperty, Parameter
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@EachProperty("stadium")  # <2>
@dataclass(init=False)
class StadiumConfiguration:
    name: str  # <3>
    city: str | None
    size: int | None

    def __init__(
        self,
        name: Annotated[str, Parameter],
        city: str | None = None,
        size: int | None = None,
    ):  # <3>
        self.name = name
        self.city = city
        self.size = size
