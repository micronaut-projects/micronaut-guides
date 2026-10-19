from jakarta.inject import Singleton
from jakarta.validation import ConstraintViolation
from jakarta.validation.Path import Node


# tag::clazz[]
@Singleton
class MessageSource:
    def violations_messages(self, violations: set[ConstraintViolation]) -> list[str]:
        return [self.violation_message(violation) for violation in violations]

    @staticmethod
    def violation_message(violation: ConstraintViolation) -> str:
        last_node: Node | None = None
        for node in violation.get_property_path():
            last_node = node

        if last_node is None:
            return violation.get_message()
        return f"{last_node.get_name()} {violation.get_message()}"
# end::clazz[]
