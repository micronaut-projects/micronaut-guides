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
package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RS256JsonWebKeyGeneratorTest {

    @Test
    fun generatesJWK() {
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            assertTrue(ctx.containsBean(RS256JsonWebKeyGenerator::class.java))

            val generator = ctx.getBean(RS256JsonWebKeyGenerator::class.java)
            val jwkJsonOptional = generator.generateJsonWebKey(null)
            assertTrue(jwkJsonOptional.isPresent)

            val jwkJson = jwkJsonOptional.get()
            assertDoesNotThrow<JWK> { JWK.parse(jwkJson) }
        }
    }
}
