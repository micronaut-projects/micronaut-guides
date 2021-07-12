package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertTrue;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class PrimarySignatureConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }

    @Test
    void primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Named.class)
                .isPresent());
        Optional<String> nameOptional = applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationValuesByType(Named.class)
                .stream()
                .filter(annValue -> annValue.getValue(String.class).isPresent())
                .map(annValue -> annValue.getValue(String.class).get())
                .findFirst();
        assertTrue(nameOptional.isPresent());
        assertEquals("generator", nameOptional.get());
    }
}
