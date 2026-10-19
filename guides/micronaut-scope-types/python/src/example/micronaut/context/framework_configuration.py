from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import Pattern
from micronaut.context.annotation import ConfigurationProperties, Context


@Context  # <1>
@ConfigurationProperties("framework")  # <2>
@dataclass
class FrameworkConfiguration:
    language: Annotated[
        str | None,
        Pattern(regexp="groovy|java|kotlin|python"),
    ] = None  # <3>
