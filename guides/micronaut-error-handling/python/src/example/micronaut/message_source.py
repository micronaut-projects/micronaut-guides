from jakarta.inject import Singleton
from jakarta.validation import ConstraintViolation


# tag::clazz[]
@Singleton
class MessageSource:
    def violations_messages(self, violations: set[ConstraintViolation]) -> list[str]:
        return [self.violation_message(violation) for violation in violations]

    @staticmethod
    def violation_message(violation: ConstraintViolation) -> str:
        last_node = None
        for node in violation.getPropertyPath():
            last_node = node

        if last_node is None:
            return violation.getMessage()
        return f"{last_node.getName()} {violation.getMessage()}"
# end::clazz[]
