package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.exceptions.BeanInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ContextTest {

    @Test
    void lifeCycleOfClassesAnnotatedWithAtContextIsBoundToThatOfTheBeanContext() {
        Executable e = () -> ApplicationContext.run(Collections.singletonMap("micronaut.language", "scala"));
        BeanInstantiationException thrown = assertThrows(BeanInstantiationException.class, e);
        assertTrue(thrown.getMessage().contains("language - must match \"groovy|java|kotlin\""));
    }
}