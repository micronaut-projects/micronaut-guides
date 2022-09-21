package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class GreetingControllerTest {

    @Inject
    GreetingClient greetingClient;

    @Test
    void testGreetingService() {
        assertEquals(
                "Hola, John!",
                greetingClient.greet("John").getContent()
        );
    }
}
