package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContactRepositorySpec extends Specification {

    @Inject
    ContactRepository contactRepository // <2>

    void "findByFirstName validates its parameter"(String firstName) {
        when:
        contactRepository.findByFirstName(firstName) // <3>

        then:
        thrown(ConstraintViolationException)

        where:
        firstName << ["", null]
    }
}
