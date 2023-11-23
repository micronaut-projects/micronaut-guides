package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import jakarta.validation.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(startApplication = false) // <1>
class FruitRepositoryTest {

    @Inject
    FruitRepository fruitRepository;

    @Test
    void methodsValidateParamers() {
        Executable e = () -> fruitRepository.create(new FruitCommand(""));
        assertThrows(ConstraintViolationException.class, e);
    }
}
