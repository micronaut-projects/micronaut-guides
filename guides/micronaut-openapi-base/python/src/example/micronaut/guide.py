from dataclasses import dataclass
from typing import Annotated

import java
from jakarta.validation import Valid
from jakarta.validation.constraints import NotBlank, NotNull, Size
from java.time import LocalDate
from micronaut.serde.annotation import Serdeable

from .option import Option


Schema = java.type("io.swagger.v3.oas.annotations.media.Schema")


@Serdeable  # <1>
@dataclass
class Guide:
    title: Annotated[str, NotBlank]
    intro: Annotated[str, NotBlank]
    authors: Annotated[list[str], NotNull, Size(min=1)]  # <2>
    tags: list[str] | None
    categories: Annotated[list[str], NotNull, Size(min=1)]
    publicationDate: Annotated[
        LocalDate,
        NotNull,
        Schema(format="yyyy-MM-dd", example="2018-05-23"),
    ]  # <3>
    slug: Annotated[str, NotBlank]
    url: Annotated[str, NotBlank]
    options: Annotated[list[Option], NotNull, Size(min=1), Valid]
