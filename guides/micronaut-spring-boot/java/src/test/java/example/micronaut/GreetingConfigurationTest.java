package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class GreetingConfigurationTest {
    @Inject
    BeanContext beanContext;

    @Test
    void testSpringConfigurationProperties() {
        assertTrue(beanContext.containsBean(GreetingConfiguration.class));
        GreetingConfiguration greetingConfiguration = beanContext.getBean(GreetingConfiguration.class);
        assertEquals("Hola, %s!", greetingConfiguration.getTemplate());
    }
}
