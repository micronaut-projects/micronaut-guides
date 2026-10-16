from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class LinkedInMe:
    id: Annotated[str, NotBlank]
    localizedFirstName: Annotated[str, NotBlank]
    localizedLastName: Annotated[str, NotBlank]
