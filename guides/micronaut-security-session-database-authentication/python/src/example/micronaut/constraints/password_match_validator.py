from jakarta.inject import Singleton
from micronaut.context.annotation import Factory
from micronaut.core.annotation import Introspected
from micronaut.validation.validator.constraints import ConstraintValidator

from ..controllers.sign_up_form import SignUpForm
from .password_match import PasswordMatch


@Introspected  # <2>
class PasswordMatchValidator(ConstraintValidator[PasswordMatch, SignUpForm]):
    def isValid(self, value: SignUpForm | None, annotation_metadata, context) -> bool:
        if value is None:
            return True
        if value.password is None and value.repeatPassword is None:
            return True
        if value.password is None or value.repeatPassword is None:
            return False
        return value.password == value.repeatPassword


@Factory
class PasswordMatchValidatorFactory:
    @Singleton
    def password_match_validator(self) -> ConstraintValidator[PasswordMatch, SignUpForm]:
        return PasswordMatchValidator()
