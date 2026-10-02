from jakarta.inject import Singleton


# tag::clazz[]
@Singleton
class MessageSource:
    def violations_messages(self, violations) -> list[str]:
        return [self.violation_message(violation) for violation in violations]

    @staticmethod
    def violation_message(violation) -> str:
        last_node = None
        for node in violation.getPropertyPath():
            last_node = node

        parts = []
        if last_node is not None:
            parts.append(last_node.getName())
        parts.append(violation.getMessage())
        return " ".join(parts)
# end::clazz[]
