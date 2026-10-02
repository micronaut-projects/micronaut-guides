import java

from jakarta.validation import Constraint

Class = java.type("java.lang.Class")

MESSAGE = "example.micronaut.E164.message"
MESSAGE_TEMPLATE = "{" + MESSAGE + "}"


@Constraint(validatedBy=[])
def E164(
    message: str = "{example.micronaut.E164.message}",
    groups: list[Class] = [],
    payload: list[Class] = [],
):
    def decorator(bean):
        return bean

    return decorator
