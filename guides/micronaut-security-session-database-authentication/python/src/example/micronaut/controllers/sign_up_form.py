from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.core.annotation import Introspected
from micronaut.serde.annotation import Serdeable
from micronaut.views.fields.annotations import InputPassword

from ..constraints.password_match import PasswordMatch


@PasswordMatch()  # <1>
@Introspected
@Serdeable  # <2>
@dataclass
class SignUpForm:
    username: Annotated[str, NotBlank]  # <3>
    password: Annotated[str, InputPassword, NotBlank]  # <4>
    repeatPassword: Annotated[str, InputPassword, NotBlank]  # <4>
