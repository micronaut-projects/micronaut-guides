package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.validation.ConstraintViolationException

@MicronautTest(startApplication = false) // <1>
class FruitRepositoryTest {

    @Inject
    lateinit var fruitRepository: FruitRepository

    @Test
    fun methodsValidateParameters() {
        Assertions.assertThrows(ConstraintViolationException::class.java) {
            fruitRepository.create(FruitCommand(""))
        }
    }
}