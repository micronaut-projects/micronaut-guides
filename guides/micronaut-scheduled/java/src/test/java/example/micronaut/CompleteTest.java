package example.micronaut;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.inject.Inject;

@MicronautTest
public class CompleteTest {

    @Inject
    EmbeddedApplication application;

    @Test
    void testItWorks() {
        assertTrue(application.isRunning());
    }

}
