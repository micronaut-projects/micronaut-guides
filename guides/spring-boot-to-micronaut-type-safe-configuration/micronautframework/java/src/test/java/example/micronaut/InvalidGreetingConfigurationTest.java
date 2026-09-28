package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.exceptions.BeanInstantiationException;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.runtime.Micronaut;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class InvalidGreetingConfigurationTest {
    @Test
    void invalidGreetingConfiguration() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of("greeting.content.prize-amount", -1))) {
            assertThrows(BeanInstantiationException.class, () -> ctx.getBean(GreetingConfiguration.class));
        }
    }
}