import java

from jakarta.validation import Constraint

Class = java.type("java.lang.Class")

MESSAGE = "example.micronaut.constraints.PasswordMatch.message"
MESSAGE_TEMPLATE = "{" + MESSAGE + "}"


@Constraint(validatedBy=[])
def PasswordMatch(
    message: str = MESSAGE_TEMPLATE,
    groups: list[Class] = [],
    payload: list[Class] = [],
):
    def decorator(bean):
        return bean

    return decorator
