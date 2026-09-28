from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.serde.annotation import Serdeable

from .build_tool import BuildTool
from .language import Language


@Serdeable  # <1>
@dataclass
class Option:
    language: Annotated[Language, NotNull]  # <2>
    buildTool: Annotated[BuildTool, NotNull]  # <2>
    url: Annotated[str, NotBlank]  # <2>
