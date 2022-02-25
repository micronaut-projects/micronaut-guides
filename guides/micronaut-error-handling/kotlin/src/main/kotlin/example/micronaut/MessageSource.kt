package example.micronaut

import jakarta.inject.Singleton
import javax.validation.ConstraintViolation
import javax.validation.Path

@Singleton
class MessageSource {

    fun violationsMessages(violations: Set<ConstraintViolation<*>>): List<String> =
            violations.map { violation: ConstraintViolation<*> -> violationMessage(violation) }

    companion object {

        private fun violationMessage(violation: ConstraintViolation<*>): String {
            val sb = StringBuilder()
            val lastNode = lastNode(violation.propertyPath)
            if (lastNode != null) {
                sb.append(lastNode.name)
                sb.append(" ")
            }
            sb.append(violation.message)
            return sb.toString()
        }

        private fun lastNode(path: Path): Path.Node? {
            var lastNode: Path.Node? = null
            for (node in path) {
                lastNode = node
            }
            return lastNode
        }
    }
}
