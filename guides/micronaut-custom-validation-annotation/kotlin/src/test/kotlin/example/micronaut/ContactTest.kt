package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false) // <1>
class ContactTest(private val validator: Validator) { // <2>

    @Test
    fun contactValidation() {
        assertTrue(validator.validate(Contact("+14155552671")).isEmpty())
        val violationSet = validator.validate(Contact("+1-4155552671"))
        assertFalse(violationSet.isEmpty())

        val template = "{example.micronaut.E164.message}"

        assertTrue(
            violationSet
                .any {
                    it.messageTemplate == template &&
                            it.invalidValue == "+1-4155552671" &&
                            it.message == "must be a phone in E.164 format"
                })
    }

}