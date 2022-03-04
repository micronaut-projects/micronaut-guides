package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class JsonWebKeysProviderTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void jsonWebKeysProviderIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(JsonWebKeysProvider.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }
}
