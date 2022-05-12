package example.micronaut;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MicronautguideTest extends BaseMicroStreamTest {

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }
}
