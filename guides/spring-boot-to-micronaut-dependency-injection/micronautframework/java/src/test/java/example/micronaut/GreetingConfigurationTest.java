package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GreetingConfigurationTest {
    @Test
    void greetingConfiguration(GreetingConfiguration configuration) {
        assertEquals("Hello", configuration.getMessage());
        assertEquals("World", configuration.getName());
        assertEquals(10, configuration.getContent().getPrizeAmount());
        assertEquals(List.of("Jane", "John"), configuration.getContent().getRecipients());
    }
}