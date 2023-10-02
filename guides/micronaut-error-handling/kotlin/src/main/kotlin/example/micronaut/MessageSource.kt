package example.micronaut

import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolation

@Singleton
class MessageSource {

    fun violationsMessages(violations: Set<ConstraintViolation<*>>): List<String> {
        return violations.map { violationMessage(it) }.toList()
    }

    private fun violationMessage(violation: ConstraintViolation<*>): String {
        val lastNode = violation.propertyPath.map { it.name + " " }.lastOrNull()
        return (lastNode ?: "") + violation.message
    }
}