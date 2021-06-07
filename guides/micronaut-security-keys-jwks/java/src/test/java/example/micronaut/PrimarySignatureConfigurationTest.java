package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@MicronautTest(startApplication = false)
public class PrimarySignatureConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        Assertions.assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }

    @Test
    void primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        Assertions.assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Named.class)
                .isPresent());
        Optional<String> nameOptional = applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationValuesByType(Named.class)
                .stream()
                .filter(annValue -> annValue.getValue(String.class).isPresent())
                .map(annValue -> annValue.getValue(String.class).get())
                .findFirst();
        Assertions.assertTrue(nameOptional.isPresent());
        Assertions.assertEquals("generator", nameOptional.get());
    }
}
