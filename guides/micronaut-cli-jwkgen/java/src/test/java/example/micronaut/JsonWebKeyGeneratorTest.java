package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonWebKeyGeneratorTest {
    @Test
    void beanOfTypeJsonWebKeyGeneratorExists() {
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            assertTrue(ctx.containsBean(JsonWebKeyGenerator.class));
        }
    }
}
