package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.security.oauth2.client.IdTokenClaimsValidator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class IdTokenClaimsValidatorTest {

    @Inject
    BeanContext beanContext;

    @Test
    void replacementWorks() {
        IdTokenClaimsValidator bean = assertDoesNotThrow(() -> beanContext.getBean(IdTokenClaimsValidator.class));
        assertInstanceOf(IdTokenClaimsValidatorReplacement.class, bean);
    }
}