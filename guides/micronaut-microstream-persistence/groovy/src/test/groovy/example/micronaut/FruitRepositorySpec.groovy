package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import jakarta.validation.ConstraintViolationException

@MicronautTest(startApplication = false) // <1>
class FruitRepositorySpec extends Specification {

    @Inject
    FruitRepository fruitRepository

    void "methods validate parameters"() {
        when:
        fruitRepository.create(new FruitCommand(""))

        then:
        thrown(ConstraintViolationException)
    }
}
