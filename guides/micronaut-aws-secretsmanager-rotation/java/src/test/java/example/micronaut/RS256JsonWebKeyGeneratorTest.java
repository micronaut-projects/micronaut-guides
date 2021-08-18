package example.micronaut;

import com.nimbusds.jose.jwk.JWK;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RS256JsonWebKeyGeneratorTest {

    @Test
    void generatesJWK() {
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            assertTrue(ctx.containsBean(RS256JsonWebKeyGenerator.class));
            RS256JsonWebKeyGenerator generator = ctx.getBean(RS256JsonWebKeyGenerator.class);
            Optional<String> jwkJsonOptional = generator.generateJsonWebKey(null);
            assertTrue(jwkJsonOptional.isPresent());
            String jwkJson = jwkJsonOptional.get();
            assertDoesNotThrow(() -> JWK.parse(jwkJson));
        }
    }
}
