from jakarta.validation import Constraint

MESSAGE = "example.micronaut.E164.message"
MESSAGE_TEMPLATE = "{" + MESSAGE + "}"


@Constraint(validatedBy=[])
def E164(
    message: str = "{example.micronaut.E164.message}",
    groups: list[type] = [],
    payload: list[type] = [],
):
    def decorator(bean):
        return bean

    return decorator
