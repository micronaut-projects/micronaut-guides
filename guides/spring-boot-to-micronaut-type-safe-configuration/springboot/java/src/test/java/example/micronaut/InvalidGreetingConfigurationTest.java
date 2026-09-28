package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindException;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "greeting.content.prize-amount=-1",
        "spring.main.lazy-initialization=true"
})
class InvalidGreetingConfigurationTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    void invalidGreetingConfiguration() {
        assertThrows(ConfigurationPropertiesBindException.class, () -> ctx.getBean(GreetingConfiguration.class));
    }
}