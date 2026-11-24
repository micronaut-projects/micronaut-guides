package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.validation.Validator
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactRequiredFieldSpec extends Specification {

    @Inject
    Validator validator // <2>

    void "firstName cannot be blank"(String firstName) {
        given:
        Contact contact = new Contact(firstName, "del Amo", null)

        expect:
        !validator.validate(contact).isEmpty() // <3>

        where:
        firstName << ["", null]
    }
}
