package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class CompositeColorFetcherTest {

    @Inject
    BeanContext beanContext;

    @Test
    void compositeColorFetcherIsThePrimaryBeanOfTypeColorFetcher() {
        assertTrue(beanContext.getBean(ColorFetcher.class) instanceof CompositeColorFetcher);
    }
}