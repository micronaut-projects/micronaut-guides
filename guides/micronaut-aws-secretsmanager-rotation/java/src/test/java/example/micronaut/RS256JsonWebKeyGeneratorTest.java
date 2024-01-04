/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
