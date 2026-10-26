package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolation
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactOptionalFieldSpec extends Specification {

    @Inject
    Validator validator // <2>

    void "company can be null"() {
        given:
        String company = null

        when:
        Set<ConstraintViolation<Contact>> violations =
                validator.validate(new Contact("Sergio", "del Amo", company)) // <3>

        then:
        violations.isEmpty()
    }
}
