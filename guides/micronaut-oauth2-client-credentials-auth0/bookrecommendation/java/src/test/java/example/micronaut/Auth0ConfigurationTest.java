package example.micronaut;

import io.micronaut.context.BeanContext;
import jakarta.inject.Inject;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class Auth0ConfigurationTest {
    @Inject
    BeanContext beanContext;

    @Test
    public void testBeanOfTypeAuth0ConfigurationExists() {
        assertEquals("https://micronautguides.eu.auth0.com/api/v2/",
            beanContext.getBean(Auth0Configuration.class).getApiIdentifier());
    }
}
