from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.core.annotation import Introspected, ReflectiveAccess

from .e164 import E164


@ReflectiveAccess
@Introspected
@dataclass
class Contact:
    phone: Annotated[str, E164, NotBlank]
