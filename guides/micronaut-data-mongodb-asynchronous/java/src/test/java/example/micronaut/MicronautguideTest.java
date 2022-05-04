package example.micronaut;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MicronautguideTest extends BaseMongoDataTest {

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
