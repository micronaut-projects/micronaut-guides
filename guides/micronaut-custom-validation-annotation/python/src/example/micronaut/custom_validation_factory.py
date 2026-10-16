from jakarta.inject import Singleton
from micronaut.context.annotation import Factory
from micronaut.validation.validator.constraints import ConstraintValidator

from .e164 import E164
from .e164_utils import is_valid


class E164Validator(ConstraintValidator[E164, str]):
    def isValid(self, value: str | None, annotation_metadata, context) -> bool:
        return is_valid(value)


@Factory  # <1>
class CustomValidationFactory:
    @Singleton  # <2>
    def e164_validator(self) -> ConstraintValidator[E164, str]:
        return E164Validator()
