package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

@MicronautTest(startApplication = false)
public class JsonWebKeysProviderTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    void jsonWebKeysProviderIsAnnotatedWithRefreshable() {
        Assertions.assertTrue(applicationContext.getBeanDefinition(JsonWebKeysProvider.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }
}
