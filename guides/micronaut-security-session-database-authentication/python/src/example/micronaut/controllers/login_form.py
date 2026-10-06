from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.core.annotation import Introspected
from micronaut.serde.annotation import Serdeable
from micronaut.views.fields.annotations import InputPassword


@Introspected
@Serdeable  # <1>
@dataclass
class LoginForm:
    username: Annotated[str, NotBlank]  # <2>
    password: Annotated[str, InputPassword, NotBlank]  # <3>
