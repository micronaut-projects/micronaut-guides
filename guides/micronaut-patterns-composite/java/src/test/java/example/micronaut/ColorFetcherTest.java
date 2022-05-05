package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class ColorFetcherTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeColorFetcherExists() {
        assertTrue(beanContext.containsBean(ColorFetcher.class));
    }
}
